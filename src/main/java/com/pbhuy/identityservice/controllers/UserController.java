package com.pbhuy.identityservice.controllers;

import com.pbhuy.identityservice.dto.request.UserCreationRequest;
import com.pbhuy.identityservice.dto.request.UserUpdateRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.UserResponse;
import com.pbhuy.identityservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.createUser(request));
        return response;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setData(userService.getUsers());
        return response;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.getUserById(userId));
        return response;
    }

    @GetMapping("/profile")
    public ApiResponse<UserResponse> getUserProfile() {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.getUserProfile());
        return response;
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.updateUser(userId, request));
        return response;
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable("userId") String userId) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Delete user successfully.");
        userService.deleteUser(userId);
        return response;
    }
}
