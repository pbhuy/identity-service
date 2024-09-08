    package com.pbhuy.identityservice.enums;

    import lombok.Getter;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.HttpStatusCode;

    @Getter
    public enum ErrorCode {
        UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),

        // entity errors
        USER_ALREADY_EXISTED(1009, "{entity} already existed", HttpStatus.BAD_REQUEST),
        USER_NOT_FOUND(1004, "{entity} not found", HttpStatus.NOT_FOUND),
        ROLE_NOT_FOUND(1004, "{entity} not found", HttpStatus.NOT_FOUND),
        PERMISSION_NOT_FOUND(1004, "{entity} not found", HttpStatus.NOT_FOUND),

        // validation
        INVALID_MESSAGE_KEY(1002, "Invalid message key", HttpStatus.BAD_REQUEST),
        // request query and body
        INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
        INVALID_PASSWORD(1003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
        INVALID_EMAIL(1003, "Invalid email", HttpStatus.BAD_REQUEST),
        INVALID_DOB(1003, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
        NOT_NULL(1003, "{field} must be required, not empty", HttpStatus.BAD_REQUEST),
        // path variable
        INVALID_PERMISSION_NAME(1003, "Permission name must be 3-50 uppercase letters or underscores", HttpStatus.BAD_REQUEST),
        INVALID_ROLE_NAME(1003, "Role name must be 3-50 uppercase letters", HttpStatus.BAD_REQUEST),
        INVALID_ID(1003, "Invalid ID format", HttpStatus.BAD_REQUEST),

        // authentication
        UNAUTHORIZED(1001, "Authentication required", HttpStatus.UNAUTHORIZED),
        FORBIDDEN(1003, "Access denied", HttpStatus.FORBIDDEN),
        ;

        private final int code;
        private final String message;
        private final HttpStatusCode statusCode;

        ErrorCode(int code, String message, HttpStatusCode statusCode) {
            this.code = code;
            this.message = message;
            this.statusCode = statusCode;
        }
    }
