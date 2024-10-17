package com.document.Documentweb.service.auth;

import com.document.Documentweb.dto.authentication.AuthenticationReqDTO;
import com.document.Documentweb.dto.authentication.AuthenticationResDTO;
import com.document.Documentweb.dto.authentication.LogOutReqDTO;
import com.document.Documentweb.dto.authentication.refresh.RefreshTokenReqDTO;
import com.document.Documentweb.dto.introspect.IntrospectRequest;
import com.document.Documentweb.dto.introspect.IntrospectResponse;
import com.document.Documentweb.entity.auth.InValidateToken;
import com.document.Documentweb.entity.auth.Permission;
import com.document.Documentweb.entity.User;
import com.document.Documentweb.exception.AppException;
import com.document.Documentweb.exception.ErrorCode;
import com.document.Documentweb.repository.InValidateTokenRepository;
import com.document.Documentweb.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements IAuthenticationService {
    UserRepository userRepository;
    InValidateTokenRepository inValidateTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Override
    public AuthenticationResDTO authenticated(AuthenticationReqDTO request) {
        var user = userRepository.findByUsername(request.getUserName()).orElseThrow(
                () ->new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authentication = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authentication) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResDTO.builder()
                .token(token)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();
/*        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        //examine expiration token
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verify = signedJWT.verify(verifier) && expirationTime.after(new Date());*/
        try {
            verifyToken(token , false);
            return IntrospectResponse.builder()
                    .valid(true)
                    .build();
        } catch (AppException e) {
            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }
    }

    private String generateToken(User user) {
        //hash header algorithm
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);


        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("boockshares.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        //payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            //signer
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void logout(LogOutReqDTO request) throws ParseException, JOSEException {
        String token = request.getToken();
        SignedJWT signToken = verifyToken(token, true);

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryDate = signToken.getJWTClaimsSet().getExpirationTime();

        InValidateToken inValidateToken = InValidateToken.builder()
                .id(jit)
                .expiryTime(expiryDate)
                .build();
        inValidateTokenRepository.save(inValidateToken);

    }

    @Override
    public AuthenticationResDTO refreshToken(RefreshTokenReqDTO request) throws ParseException, JOSEException {
        //get token
        String token = request.getToken();
        SignedJWT signToken = verifyToken(token, true);


        //get id token
        String jit = signToken.getJWTClaimsSet().getJWTID();
        //get expiry Date token
        Date expiryDate = signToken.getJWTClaimsSet().getExpirationTime();

        //log out
        InValidateToken inValidateToken = InValidateToken.builder()
                .id(jit)
                .expiryTime(expiryDate)
                .build();
        inValidateTokenRepository.save(inValidateToken);

        String userName = signToken.getJWTClaimsSet().getSubject();

        User user = userRepository.findByUsername(userName).orElseThrow(
                () ->new AppException(ErrorCode.USER_NOT_EXISTED));


        String newToken = generateToken(user);

        return AuthenticationResDTO.builder()
                .token(newToken)
                .build();

    }

    private SignedJWT verifyToken(String token, boolean isRefreshToken) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        //examine expiration token
/*        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();*/

        Date expirationTime = isRefreshToken
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(120, ChronoUnit.MINUTES)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verify = signedJWT.verify(verifier) && expirationTime.after(new Date());
        if (!verify) throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (inValidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                    for (Permission permission : role.getPermissions()) {
                        stringJoiner.add(permission.getName());
                    }
                }
            });
        }
        return stringJoiner.toString();
    }


}
