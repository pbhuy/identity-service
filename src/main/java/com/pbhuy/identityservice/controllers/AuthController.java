package com.pbhuy.identityservice.controllers;

import com.nimbusds.jose.JOSEException;
import com.pbhuy.identityservice.dto.request.*;
import com.pbhuy.identityservice.dto.response.*;
import com.pbhuy.identityservice.entities.Role;
import com.pbhuy.identityservice.services.AuthService;
import com.pbhuy.identityservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "APIs for authenticating user")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "User login",
            description = "Authenticates a user and returns a token for accessing protected resources")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthRequest.class),
                    examples = {@ExampleObject(
                            name = "User Login",
                            summary = "User login",
                            value = "{\n" +
                                    " \"username\": \"john.doe\",\n" +
                                    " \"password\": \"12345678\"\n" +
                                    "}"
                    ), @ExampleObject(
                            name = "Admin Login",
                            summary = "Admin login",
                            value = "{\n" +
                                    " \"username\": \"admin\",\n" +
                                    " \"password\": \"admin\"\n" +
                                    "}"
                    )}
            )
    )
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.setData(authService.authenticate(request));
        return response;
    }

    @Operation(summary = "Token introspection",
            description = "Validate the given token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = IntrospectRequest.class),
                    examples = {@ExampleObject(
                            name = "Access Token",
                            description = "Access token",
                            value = "{" +
                                    "\"token\": \"Get token from login or register user to replace it here\"\n" +
                                    "}"
                    )}
            )
    )
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid IntrospectRequest request)
            throws ParseException, JOSEException {
        ApiResponse<IntrospectResponse> response = new ApiResponse<>();
        response.setData(authService.introspect(request));
        return response;
    }

    @Operation(summary = "User registration",
            description = "Registers a new user and returns a token for immediate access")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserCreationRequest.class),
                    examples = {@ExampleObject(
                            name = "User Registration",
                            summary = "User registration",
                            value = "{\n" +
                                    " \"username\": \"john.doe\",\n" +
                                    " \"password\": \"12345678\",\n" +
                                    " \"firstName\": \"john\",\n" +
                                    " \"lastName\": \"doe\",\n" +
                                    " \"dob\": \"2000-08-20\",\n" +
                                    " \"roles\": [\"USER\"]\n" +
                                    "}"
                    )}
            )
    )
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid UserCreationRequest request) {
        UserResponse userResponse = userService.createUser(request);
        Set<Role> roles = authService.convertPermissions(request.getRoles());
        String token = authService.generateToken(request.getUsername(), roles);
        ApiResponse<RegisterResponse> response = new ApiResponse<>();
        response.setData(new RegisterResponse(token, userResponse));
        return response;
    }

    @Operation(summary = "Refresh token",
            description = "Issues a new access token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RefreshRequest.class),
                    examples = {@ExampleObject(
                            name = "Access Token",
                            description = "Access token",
                            value = "{" +
                                    "\"token\": \"Get token from login or register user to replace it here\"\n" +
                                    "}"
                    )}
            )
    )
    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@RequestBody @Valid RefreshRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.refreshToken(request))
                .build();
    }

    @Operation(summary = "User logout", description = "Invalidates the user's token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LogoutRequest.class),
                    examples = {@ExampleObject(
                            name = "Access Token",
                            description = "Access token",
                            value = "{" +
                                    "\"token\": \"Get token from login or register user to replace it here\"\n" +
                                    "}"
                    )}
            )
    )
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request)
            throws ParseException, JOSEException {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setData(null);
        response.setMessage("Logout successful");
        authService.logout(request);
        return response;
    }
}
