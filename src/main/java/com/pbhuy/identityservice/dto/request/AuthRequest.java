package com.pbhuy.identityservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotNull(message = "NOT_NULL")
    private String username;

    @NotNull(message = "NOT_NULL")
    private String password;
}
