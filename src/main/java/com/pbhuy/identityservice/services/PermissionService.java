package com.pbhuy.identityservice.services;

import com.pbhuy.identityservice.dto.request.PermissionRequest;
import com.pbhuy.identityservice.dto.request.PermissionUpdateRequest;
import com.pbhuy.identityservice.dto.response.PermissionResponse;
import com.pbhuy.identityservice.entities.Permission;
import com.pbhuy.identityservice.enums.ErrorCode;
import com.pbhuy.identityservice.exceptions.AppException;
import com.pbhuy.identityservice.mappers.PermissionMapper;
import com.pbhuy.identityservice.repositories.PermissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionResponse> getPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionResponse> responses = new ArrayList<>();
        permissions.forEach(permission -> {
            responses.add(permissionMapper.toPermissionResponse(permission));
        });
        return responses;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse getPermission(String permissionName) {
        Permission permission = permissionRepository.findById(permissionName)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse update(String permissionName, PermissionUpdateRequest request) {
        Permission permission = permissionRepository.findById(permissionName)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        permission.setDescription(request.getDescription());
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PermissionResponse delete(String permissionName) {
        Permission permission = permissionRepository.findById(permissionName)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionRepository.delete(permission);
        return permissionMapper.toPermissionResponse(permission);
    }
}
