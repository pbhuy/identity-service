package com.pbhuy.identityservice.exceptions;

import com.pbhuy.identityservice.dto.response.ApiResponse;
import com.pbhuy.identityservice.enums.ErrorCode;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";
    private static final String MISSING_FIELD = "field";

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Void>> handelRuntimeException(RuntimeException e) {
        ApiResponse<Void> response = new ApiResponse<>();

        response.setSuccess(false);
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Void>> handelAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = new ApiResponse<>();

        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handelAuthorizationDeniedException(AuthorizationDeniedException e) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;


        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse<Void> response = new ApiResponse<>();
        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;
        Map<String, Object> attributes = null;
        String defaultMessage = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        String fieldError = "";
        try {
            errorCode = ErrorCode.valueOf(defaultMessage);
            if ("NOT_NULL".equals(defaultMessage)) {
                fieldError = Objects.requireNonNull(e.getBindingResult().getFieldError()).getField();
            } else {
                ConstraintViolation<?> constraintViolation = e.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
                attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            }
        } catch (IllegalArgumentException ignored) {
        }

        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        String errorMessage = errorCode.getMessage();
        if ("NOT_NULL".equals(defaultMessage)) {
            errorMessage = mapField(errorMessage, fieldError);
        }
        else if (Objects.nonNull(attributes)) {
            errorMessage = mapAttribute(errorMessage, attributes);
        }
        response.setMessage(errorMessage);

        return ResponseEntity.badRequest().body(response);
    }

    private static String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    private static String mapField(String message, String field) {
        return message.replace("{" + MISSING_FIELD + "}", field);
    }
}
