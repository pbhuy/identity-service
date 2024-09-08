package com.pbhuy.identityservice.controllers;

import com.pbhuy.identityservice.dto.request.RoleRequest;
import com.pbhuy.identityservice.dto.request.RoleUpdateRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.RoleResponse;
import com.pbhuy.identityservice.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Tag(name = "Role Controller", description = "APIs for managing role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Retrieve a list of all roles",
            description = "Fetches and returns a list of all roles")
    @GetMapping
    public ApiResponse<List<RoleResponse>> getPermissions() {
        List<RoleResponse> roles = roleService.getRoles();
        ApiResponse<List<RoleResponse>> response = new ApiResponse<>();
        response.setData(roles);
        return response;
    }

    @Operation(summary = "Retrieve a role by name",
            description = "Fetches and returns a role")
    @GetMapping("/{roleName}")
    public ApiResponse<RoleResponse> getPermission(
            @Parameter(description = "Uppercase letter", example = "USER")
            @PathVariable
            @Pattern(regexp = "^[A-Z]{3,50}$", message = "INVALID_ROLE_NAME")
            String roleName) {
        RoleResponse role = roleService.getRole(roleName);
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setData(role);
        return response;
    }

    @Operation(summary = "Create a new role", description = "Returns a new created role")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoleRequest.class),
                    examples = {@ExampleObject(
                            name = "Role Creation",
                            summary = "role creation",
                            value = "{\n" +
                                    " \"name\": \"STAFF\",\n" +
                                    " \"description\": \"Staff role\",\n" +
                                    " \"permissions\": [\"APPROVE_POST\", \"REJECT_POST\"]\n" +
                                    "}"
                    )}
            )
    )
    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody @Valid RoleRequest request) {
        RoleResponse role = roleService.create(request);
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setMessage("Create role successfully.");
        response.setData(role);
        return response;
    }

    @Operation(summary = "Update a role by name",
            description = "Returns the updated role by the give name")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoleRequest.class),
                    examples = {@ExampleObject(
                            name = "Role Update",
                            summary = "role update",
                            value = "{\n" +
                                    " \"name\": \"STAFF\",\n" +
                                    " \"description\": \"Staff role\",\n" +
                                    " \"permissions\": [\"APPROVE_POST\", \"REJECT_POST\"]\n" +
                                    "}"
                    )}
            )
    )
    @PutMapping("/{roleName}")
    public ApiResponse<RoleResponse> update(
            @Parameter(description = "Uppercase letter", example = "USER")
            @PathVariable
            @Pattern(regexp = "^[A-Z]{3,50}$", message = "INVALID_ROLE_NAME")
            String roleName,
            @RequestBody @Valid RoleUpdateRequest request) {
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setMessage("Update role successfully.");
        response.setData(roleService.update(roleName, request));
        return response;
    }

    @Operation(summary = "Delete a role by name", description = "Returns the deleted role")
    @DeleteMapping("/{roleName}")
    public ApiResponse<RoleResponse> delete(
            @Parameter(description = "Uppercase letter", example = "USER")
            @PathVariable
            @Pattern(regexp = "^[A-Z]{3,50}$", message = "INVALID_ROLE_NAME")
            String roleName) {
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setData(roleService.delete(roleName));
        response.setMessage("Delete role successfully.");
        return response;
    }
}
