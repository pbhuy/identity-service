package com.pbhuy.identityservice.dto.request;

import com.pbhuy.identityservice.validations.Adult;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String firstName;
    private String lastName;
    @Adult(min = 18, message = "INVALID_DOB")
    private LocalDate dob;
    private List<String> roles;
}
