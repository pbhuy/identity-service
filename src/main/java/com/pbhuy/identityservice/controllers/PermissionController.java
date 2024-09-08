package com.pbhuy.identityservice.controllers;

import com.pbhuy.identityservice.dto.request.PermissionRequest;
import com.pbhuy.identityservice.dto.request.PermissionUpdateRequest;
import com.pbhuy.identityservice.dto.request.RoleRequest;
import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.dto.response.PermissionResponse;
import com.pbhuy.identityservice.services.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Validated
@Tag(name = "Permission Controller", description = "APIs for managing permission")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "Retrieve a list of all permissions",
            description = "Fetches and returns a list of all permissions")
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getPermissions() {
        List<PermissionResponse> permissions = permissionService.getPermissions();
        ApiResponse<List<PermissionResponse>> response = new ApiResponse<>();
        response.setData(permissions);
        return response;
    }

    @Operation(summary = "Retrieve a permission by name",
            description = "Fetches and returns a permission")
    @GetMapping("/{permissionName}")
    public ApiResponse<PermissionResponse> getPermission(
            @Parameter(description = "Uppercase letter", example = "APPROVE_POST")
            @PathVariable
            @Pattern(regexp = "^[A-Z_]{3,50}$", message = "INVALID_PERMISSION_NAME")
            String permissionName) {
        ApiResponse<PermissionResponse> response = new ApiResponse<>();
        response.setData(permissionService.getPermission(permissionName));
        return response;
    }

    @Operation(summary = "Create a new permission", description = "Returns a new created permission")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PermissionRequest.class),
                    examples = {@ExampleObject(
                            name = "Permission Creation",
                            summary = "permission creation",
                            value = "{\n" +
                                    " \"name\": \"CREATE_POST\",\n" +
                                    " \"description\": \"Create a new post\"\n" +
                                    "}"
                    )}
            )
    )
    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest permissionRequest) {
        PermissionResponse permission = permissionService.create(permissionRequest);
        ApiResponse<PermissionResponse> response = new ApiResponse<>();
        response.setData(permission);
        return response;
    }

    @Operation(summary = "Update a permission by name",
            description = "Returns the updated permission by the give name")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PermissionUpdateRequest.class),
                    examples = {@ExampleObject(
                            name = "Permission Update",
                            summary = "Permission update",
                            value = "{\n" +
                                    " \"description\": \"Create a new post updated\"\n" +
                                    "}"
                    )}
            )
    )
    @PutMapping("/{permissionName}")
    public ApiResponse<PermissionResponse> updatePermission(
            @Parameter(description = "Uppercase letter", example = "APPROVE_POST")
            @PathVariable
            @Pattern(regexp = "^[A-Z_]{3,50}$", message = "INVALID_PERMISSION_NAME")
            String permissionName,
            @RequestBody @Valid PermissionUpdateRequest permissionRequest) {
        ApiResponse<PermissionResponse> response = new ApiResponse<>();
        response.setData(permissionService.update(permissionName, permissionRequest));
        response.setMessage("Update permission successfully.");
        return response;
    }

    @Operation(summary = "Delete a permission by name", description = "Returns the deleted permission")
    @DeleteMapping("/{permissionName}")
    public ApiResponse<PermissionResponse> deletePermission(
            @Parameter(description = "Uppercase letter", example = "APPROVE_POST")
            @PathVariable
            @Pattern(regexp = "^[A-Z_]{3,50}$", message = "INVALID_PERMISSION_NAME")
            String permissionName) {
        ApiResponse<PermissionResponse> response = new ApiResponse<>();
        response.setData(permissionService.delete(permissionName));
        response.setMessage("Delete permission successfully.");
        return response;
    }
}
