package com.pbhuy.identityservice.controllers;

import com.pbhuy.identityservice.dto.request.RoleRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.RoleResponse;
import com.pbhuy.identityservice.services.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getPermissions() {
        List<RoleResponse> roles = roleService.getRoles();
        ApiResponse<List<RoleResponse>> response = new ApiResponse<>();
        response.setData(roles);
        return response;
    }

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        RoleResponse role = roleService.create(roleRequest);
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setData(role);
        return response;
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse<Void> delete(@PathVariable("roleName") String roleName) {
        roleService.delete(roleName);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Delete role successfully.");
        return response;
    }
}
