package com.phuc.tutoring_center.core.exception;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class BusinessException extends RuntimeException {
    private final String message;
    private final int statusCode;
    private final String errorCode;

    // Predefined error codes
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    
    // Authentication error codes
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String TOKEN_INVALID = "TOKEN_INVALID";
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
    public static final String ACCOUNT_DISABLED = "ACCOUNT_DISABLED";
    public static final String INVALID_REFRESH_TOKEN = "INVALID_REFRESH_TOKEN";
    
    // Authorization error codes
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String INSUFFICIENT_PERMISSIONS = "INSUFFICIENT_PERMISSIONS";
    public static final String ROLE_REQUIRED = "ROLE_REQUIRED";
    public static final String RESOURCE_ACCESS_DENIED = "RESOURCE_ACCESS_DENIED";
    
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    // Predefined status codes
    public static final int NOT_FOUND = 404;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED_CODE = 401;
    public static final int FORBIDDEN_CODE = 403;
    public static final int INTERNAL_ERROR_CODE = 500;

    public BusinessException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
        this.errorCode = null;
    }

    public BusinessException(String message, int statusCode, String errorCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public BusinessException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.statusCode = statusCode;
        this.errorCode = null;
    }

    public BusinessException(String message, int statusCode, String errorCode, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    // Utility methods for common exceptions
    public static BusinessException notFound(String message) {
        return new BusinessException(message, NOT_FOUND, RESOURCE_NOT_FOUND);
    }

    public static BusinessException validationError(String message) {
        return new BusinessException(message, BAD_REQUEST, VALIDATION_ERROR);
    }

    public static BusinessException unauthorized(String message) {
        return new BusinessException(message, UNAUTHORIZED_CODE, UNAUTHORIZED);
    }

    public static BusinessException forbidden(String message) {
        return new BusinessException(message, FORBIDDEN_CODE, FORBIDDEN);
    }

    public static BusinessException internalError(String message) {
        return new BusinessException(message, INTERNAL_ERROR_CODE, INTERNAL_ERROR);
    }

    public static BusinessException internalError(String message, Throwable cause) {
        return new BusinessException(message, INTERNAL_ERROR_CODE, INTERNAL_ERROR, cause);
    }

    // Authentication utility methods
    public static BusinessException invalidCredentials(String message) {
        return new BusinessException(message, UNAUTHORIZED_CODE, INVALID_CREDENTIALS);
    }

    public static BusinessException tokenExpired(String message) {
        return new BusinessException(message, UNAUTHORIZED_CODE, TOKEN_EXPIRED);
    }

    public static BusinessException tokenInvalid(String message) {
        return new BusinessException(message, UNAUTHORIZED_CODE, TOKEN_INVALID);
    }

    public static BusinessException accountLocked(String message) {
        return new BusinessException(message, UNAUTHORIZED_CODE, ACCOUNT_LOCKED);
    }

    public static BusinessException accountDisabled(String message) {
        return new BusinessException(message, UNAUTHORIZED_CODE, ACCOUNT_DISABLED);
    }

    public static BusinessException invalidRefreshToken(String message) {
        return new BusinessException(message, UNAUTHORIZED_CODE, INVALID_REFRESH_TOKEN);
    }

    // Authorization utility methods
    public static BusinessException insufficientPermissions(String message) {
        return new BusinessException(message, FORBIDDEN_CODE, INSUFFICIENT_PERMISSIONS);
    }

    public static BusinessException roleRequired(String message) {
        return new BusinessException(message, FORBIDDEN_CODE, ROLE_REQUIRED);
    }

    public static BusinessException resourceAccessDenied(String message) {
        return new BusinessException(message, FORBIDDEN_CODE, RESOURCE_ACCESS_DENIED);
    }

    @Override
    public String toString() {
        return String.format("BusinessException{message='%s', statusCode=%d, errorCode='%s'}", 
            message, statusCode, errorCode);
    }
}
