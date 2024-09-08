package com.pbhuy.identityservice.dto.request;

import com.pbhuy.identityservice.validations.Adult;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {

    @NotNull(message = "NOT_NULL")
    @Size(min = 3, message = "INVALID_USERNAME")
    private String username;

    @NotNull(message = "NOT_NULL")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;

    @NotNull(message = "NOT_NULL")
    private String firstName;

    @NotNull(message = "NOT_NULL")
    private String lastName;

    @NotNull(message = "NOT_NULL")
    @Adult(min = 18, message = "INVALID_DOB")
    private LocalDate dob;

    private List<String> roles;
}
