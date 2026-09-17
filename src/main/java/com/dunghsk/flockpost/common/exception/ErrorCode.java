package com.dunghsk.flockpost.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Access denied"),
    CONFLICT(HttpStatus.CONFLICT, "Conflict"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    // Domain-specific - thêm dần khi các package khác cần
    CREATURE_NOT_TRAINED(HttpStatus.BAD_REQUEST, "Creature does not have enough exp for this delivery"),
    CREATURE_DEAD(HttpStatus.CONFLICT, "This creature has died and must be repurchased");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String defaultMessage) {
        this.status = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}
