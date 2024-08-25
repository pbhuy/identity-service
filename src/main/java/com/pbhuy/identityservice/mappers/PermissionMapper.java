package com.pbhuy.identityservice.mappers;

import com.pbhuy.identityservice.dto.request.PermissionRequest;
import com.pbhuy.identityservice.dto.response.PermissionResponse;
import com.pbhuy.identityservice.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
