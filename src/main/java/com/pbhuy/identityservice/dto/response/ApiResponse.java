package com.pbhuy.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success = true;
    private int code = 1000;
    private String message;
    private T data;
}
