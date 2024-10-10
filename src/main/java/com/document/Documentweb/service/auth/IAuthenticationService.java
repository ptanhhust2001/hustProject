package com.document.Documentweb.service.auth;

import com.document.Documentweb.dto.authentication.AuthenticationReqDTO;
import com.document.Documentweb.dto.authentication.AuthenticationResDTO;
import com.document.Documentweb.dto.authentication.LogOutReqDTO;
import com.document.Documentweb.dto.authentication.refresh.RefreshTokenReqDTO;
import com.document.Documentweb.dto.introspect.IntrospectRequest;
import com.document.Documentweb.dto.introspect.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResDTO authenticated(AuthenticationReqDTO request);

    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;

    void logout(LogOutReqDTO request) throws ParseException, JOSEException;

    AuthenticationResDTO refreshToken(RefreshTokenReqDTO request) throws ParseException, JOSEException;
}
