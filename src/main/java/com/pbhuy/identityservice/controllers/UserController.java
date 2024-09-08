package com.pbhuy.identityservice.controllers;

import com.pbhuy.identityservice.dto.request.UserCreationRequest;
import com.pbhuy.identityservice.dto.request.UserUpdateRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.UserResponse;
import com.pbhuy.identityservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "User Controller", description = "APIs for managing user")
public class UserController {
    private final String UUID_FORMAT = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user",
            description = "Creates and returns the new user's details")
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
    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.createUser(request));
        return response;
    }

    @Operation(summary = "Retrieve a list of all users",
            description = "Fetches and returns a list of all users registered")
    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setData(userService.getUsers());
        return response;
    }

    @Operation(summary = "Retrieve a user by id",
            description = "Fetches and returns a user by the given id")
    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(
            @Parameter(description = "UUID format", example = "d81d783a-6f2b-4e13-9a9e-77af2fdc8cad")
            @PathVariable
            @Pattern(regexp = UUID_FORMAT, message = "INVALID_ID")
            String userId) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.getUserById(userId));
        return response;
    }

    @Operation(summary = "Retrieve a user",
            description = "Fetches and returns the profile details of the authenticated user")
    @GetMapping("/profile")
    public ApiResponse<UserResponse> getUserProfile() {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.getUserProfile());
        return response;
    }

    @Operation(summary = "Update a user by id",
            description = "Returns a updated user by the given id")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserUpdateRequest.class),
                    examples = {@ExampleObject(
                            name = "User Registration",
                            summary = "User registration",
                            value = "{\n" +
                                    " \"password\": \"12345678\",\n" +
                                    " \"firstName\": \"john\",\n" +
                                    " \"lastName\": \"doe\",\n" +
                                    " \"dob\": \"2000-08-20\",\n" +
                                    " \"roles\": [\"USER\"]\n" +
                                    "}"
                    )}
            )
    )
    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @Parameter(description = "UUID format", example = "d81d783a-6f2b-4e13-9a9e-77af2fdc8cad")
            @PathVariable
            @Pattern(regexp = UUID_FORMAT, message = "INVALID_ID")
            String userId,
            @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.updateUser(userId, request));
        return response;
    }

    @Operation(summary = "Delete a user by id", description = "Returns a deleted user by the given id")
    @DeleteMapping("/{userId}")
    public ApiResponse<UserResponse> deleteUser(
            @Parameter(description = "UUID format", example = "d81d783a-6f2b-4e13-9a9e-77af2fdc8cad")
            @PathVariable
            @Pattern(regexp = UUID_FORMAT, message = "INVALID_ID")
            String userId) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setMessage("Delete user successfully.");
        response.setData(userService.delete(userId));
        return response;
    }
}
