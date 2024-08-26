package com.pbhuy.identityservice.controllers;

import com.nimbusds.jose.JOSEException;
import com.pbhuy.identityservice.dto.request.AuthRequest;
import com.pbhuy.identityservice.dto.request.IntrospectRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.AuthResponse;
import com.pbhuy.identityservice.dto.response.IntrospectResponse;
import com.pbhuy.identityservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setData(authService.authenticate(request));
        return response;
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid IntrospectRequest request)
            throws ParseException, JOSEException {
        ApiResponse<IntrospectResponse> response = new ApiResponse<>();
        response.setData(authService.introspect(request));
        return response;
    }
}
