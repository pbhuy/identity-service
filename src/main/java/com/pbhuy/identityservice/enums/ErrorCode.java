    package com.pbhuy.identityservice.enums;

    import lombok.Getter;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.HttpStatusCode;

    @Getter
    public enum ErrorCode {
        UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),

        USER_ALREADY_EXISTED(1002, "{entity} already existed", HttpStatus.BAD_REQUEST),
        USER_NOT_FOUND(1005, "{entity} not found", HttpStatus.NOT_FOUND),
        // field validation
        INVALID_MESSAGE_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
        INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
        INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
        INVALID_EMAIL(1009, "Invalid email", HttpStatus.BAD_REQUEST),
        INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
        NOT_NULL(1009, "{field} must be required, not empty", HttpStatus.BAD_REQUEST),
        // authentication
        UNAUTHORIZED(1006, "Authentication required", HttpStatus.UNAUTHORIZED),
        FORBIDDEN(1007, "Access denied", HttpStatus.FORBIDDEN),
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
