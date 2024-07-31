package com.pbhuy.identityservice.controllers;

import com.pbhuy.identityservice.dto.request.AuthRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.AuthResponse;
import com.pbhuy.identityservice.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        boolean result = authService.authenticate(request);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(result);
        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setData(authResponse);
        return response;
    }
}
