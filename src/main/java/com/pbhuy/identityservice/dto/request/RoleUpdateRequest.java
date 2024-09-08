package com.pbhuy.identityservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {
    @NotNull(message = "NOT_NULL")
    private String description;
    @NotNull(message = "NOT_NULL")
    private Set<String> permissions;
}
