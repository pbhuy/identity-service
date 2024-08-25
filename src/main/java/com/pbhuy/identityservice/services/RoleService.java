package com.pbhuy.identityservice.services;

import com.pbhuy.identityservice.dto.request.RoleRequest;
import com.pbhuy.identityservice.dto.response.RoleResponse;
import com.pbhuy.identityservice.entities.Permission;
import com.pbhuy.identityservice.entities.Role;
import com.pbhuy.identityservice.mappers.RoleMapper;
import com.pbhuy.identityservice.repositories.PermissionRepository;
import com.pbhuy.identityservice.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        log.info("Permissions: {}", permissions);
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = new ArrayList<>();
        roles.forEach(role -> {
            roleResponses.add(roleMapper.toRoleResponse(role));
        });
        return roleResponses;
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
