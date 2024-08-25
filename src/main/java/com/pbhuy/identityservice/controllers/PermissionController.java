package com.pbhuy.identityservice.controllers;

import com.pbhuy.identityservice.dto.request.PermissionRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.PermissionResponse;
import com.pbhuy.identityservice.services.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getPermissions() {
        List<PermissionResponse> permissions = permissionService.getPermissions();
        ApiResponse<List<PermissionResponse>> response = new ApiResponse<>();
        response.setData(permissions);
        return response;
    }

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        PermissionResponse permission = permissionService.create(permissionRequest);
        ApiResponse<PermissionResponse> response = new ApiResponse<>();
        response.setData(permission);
        return response;
    }

    @DeleteMapping("/{permissionName}")
    public ApiResponse<Void> deletePermission(@PathVariable("permissionName") String permissionName) {
        permissionService.delete(permissionName);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Delete permission successfully.");
        return response;
    }
}
