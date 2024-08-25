package com.pbhuy.identityservice.dto.request;

import com.pbhuy.identityservice.validations.Adult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {

    @Size(min = 3, message = "INVALID_USERNAME")
    private String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

    @NotNull(message = "NOT_NULL")
    private String firstName;

    @NotNull(message = "NOT_NULL")
    private String lastName;

    @NotNull(message = "NOT_NULL")
    @Adult(min = 20, message = "INVALID_DOB")
    private LocalDate dob;
}
