package com.dunghsk.flockpost.common.exception;

import lombok.Getter;

/**
 * Exception gốc cho toàn bộ lỗi nghiệp vụ có chủ đích (không phải bug).
 * Mọi exception domain-specific (CreatureNotTrainedException, MessageBlockedException...)
 * nên extends class này thay vì RuntimeException trực tiếp, để GlobalExceptionHandler
 * xử lý được tất cả bằng 1 @ExceptionHandler(ApiException.class) duy nhất.
 */

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
