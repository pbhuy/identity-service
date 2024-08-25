package com.pbhuy.identityservice.mappers;

import com.pbhuy.identityservice.dto.request.PermissionRequest;
import com.pbhuy.identityservice.dto.request.RoleRequest;
import com.pbhuy.identityservice.dto.response.PermissionResponse;
import com.pbhuy.identityservice.dto.response.RoleResponse;
import com.pbhuy.identityservice.entities.Permission;
import com.pbhuy.identityservice.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
