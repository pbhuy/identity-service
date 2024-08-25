package com.pbhuy.identityservice.services;

import com.pbhuy.identityservice.dto.request.PermissionRequest;
import com.pbhuy.identityservice.dto.response.PermissionResponse;
import com.pbhuy.identityservice.entities.Permission;
import com.pbhuy.identityservice.mappers.PermissionMapper;
import com.pbhuy.identityservice.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {
    private PermissionRepository permissionRepository;
    private PermissionMapper permissionMapper;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionResponse> responses = new ArrayList<>();
        permissions.forEach(permission -> {
            responses.add(permissionMapper.toPermissionResponse(permission));
        });
        return responses;
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
