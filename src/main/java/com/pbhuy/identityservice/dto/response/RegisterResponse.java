package com.pbhuy.identityservice.dto.response;

import com.pbhuy.identityservice.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String token;
    private UserResponse user;
}
