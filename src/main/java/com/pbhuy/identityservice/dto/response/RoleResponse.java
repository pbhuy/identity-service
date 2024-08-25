package com.pbhuy.identityservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}
