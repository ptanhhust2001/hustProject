package com.document.Documentweb.controller;

import com.document.Documentweb.dto.ResponseDTO;
import com.document.Documentweb.dto.authentication.AuthenticationReqDTO;
import com.document.Documentweb.dto.authentication.AuthenticationResDTO;
import com.document.Documentweb.dto.authentication.LogOutReqDTO;
import com.document.Documentweb.dto.authentication.refresh.RefreshTokenReqDTO;
import com.document.Documentweb.dto.introspect.IntrospectRequest;
import com.document.Documentweb.dto.introspect.IntrospectResponse;
import com.document.Documentweb.service.auth.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenController {
    private final AuthenticationService service;

    @PostMapping("/token")
    public ResponseDTO<AuthenticationResDTO> authentication(@RequestBody AuthenticationReqDTO request) {
        AuthenticationResDTO authenticationResDTO = service.authenticated(request);
        return ResponseDTO.success(authenticationResDTO);
    }

    @PostMapping("/introspect")
    public ResponseDTO<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse introspectResponse = service.introspect(request);
        return ResponseDTO.success(introspectResponse);
    }

    @PostMapping("/logout")
    public ResponseDTO<Void> logOut(@RequestBody LogOutReqDTO request) throws ParseException, JOSEException {
        service.logout(request);
        return ResponseDTO.success();
    }

    @PostMapping("/refresh")
    public ResponseDTO<AuthenticationResDTO> refreshToken(@RequestBody RefreshTokenReqDTO request) throws ParseException, JOSEException {
        return ResponseDTO.success(service.refreshToken(request));
    }

}
