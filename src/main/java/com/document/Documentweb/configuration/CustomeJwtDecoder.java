package com.document.Documentweb.configuration;

import com.document.Documentweb.dto.introspect.IntrospectRequest;
import com.document.Documentweb.dto.introspect.IntrospectResponse;
import com.document.Documentweb.service.auth.IAuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

//custom logout
@Component
@RequiredArgsConstructor
public class CustomeJwtDecoder implements JwtDecoder {
    private String signerKey = "3QCHBCeWYk5Nvf23KERk8Z45Bv3BHH3HZALSLWKq+gukKZP9ksb9/rvIVIUqqdFN";

    private final IAuthenticationService authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            IntrospectResponse introspectResponse = authenticationService.introspect(IntrospectRequest.builder()
                            .token(token)
                    .build());
            if (!introspectResponse.isValid()) throw new JwtException("Token invalid");
        } catch (ParseException | JOSEException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
