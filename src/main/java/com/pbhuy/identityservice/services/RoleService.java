package com.pbhuy.identityservice.services;

import com.pbhuy.identityservice.dto.request.RoleRequest;
import com.pbhuy.identityservice.dto.request.RoleUpdateRequest;
import com.pbhuy.identityservice.dto.response.PermissionResponse;
import com.pbhuy.identityservice.dto.response.RoleResponse;
import com.pbhuy.identityservice.entities.Permission;
import com.pbhuy.identityservice.entities.Role;
import com.pbhuy.identityservice.enums.ErrorCode;
import com.pbhuy.identityservice.exceptions.AppException;
import com.pbhuy.identityservice.mappers.RoleMapper;
import com.pbhuy.identityservice.repositories.PermissionRepository;
import com.pbhuy.identityservice.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.permissionRepository = permissionRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        log.info("Permissions: {}", permissions);
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = new ArrayList<>();
        roles.forEach(role -> {
            roleResponses.add(roleMapper.toRoleResponse(role));
        });
        return roleResponses;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse getRole(String roleName) {
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse update(String roleName, RoleUpdateRequest request) {
        if (!roleRepository.existsByName(roleName)) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        Role role = roleRepository.findByName(roleName);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setDescription(request.getDescription());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleResponse delete(String roleName) {
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleRepository.delete(role);
        return roleMapper.toRoleResponse(role);
    }
}
