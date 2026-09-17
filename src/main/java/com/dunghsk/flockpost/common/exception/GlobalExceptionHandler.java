package com.dunghsk.flockpost.common.exception;

import com.dunghsk.flockpost.common.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Bắt toàn bộ exception ở 1 chỗ duy nhất, không rải try-catch trong Controller/Service.
 * Thêm handler mới cho exception mới thay vì bọc try-catch riêng ở từng nơi throw ra.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Mọi lỗi nghiệp vụ có chủ đích (ApiException và các lớp con) đi qua đây. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        log.warn("ApiException [{}]: {}", ex.getErrorCode(), ex.getMessage());

        ErrorResponse body = ErrorResponse.builder()
                .success(false)
                .errorCode(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(body);
    }

    /** Lỗi validate @Valid trên request body (DTO có @NotBlank, @Email...). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ErrorResponse.FieldError.builder()
                        .field(fe.getField())
                        .message(fe.getDefaultMessage())
                        .build())
                .toList();

        ErrorResponse body = ErrorResponse.builder()
                .success(false)
                .errorCode(ErrorCode.VALIDATION_FAILED.name())
                .message(ErrorCode.VALIDATION_FAILED.getDefaultMessage())
                .fieldErrors(fieldErrors)
                .build();
        return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.getStatus()).body(body);
    }

    /** Lỗi validate trên @RequestParam/@PathVariable (dùng @Validated ở class Controller). */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .success(false)
                .errorCode(ErrorCode.VALIDATION_FAILED.name())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.getStatus()).body(body);
    }

    /**
     * Lưới an toàn cuối cùng - bug thật sự (NullPointerException, lỗi DB...).
     * Log đầy đủ stacktrace để debug, nhưng KHÔNG trả chi tiết lỗi về cho client
     * (tránh leak thông tin nội bộ như tên bảng, query SQL).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        ErrorResponse body = ErrorResponse.builder()
                .success(false)
                .errorCode(ErrorCode.INTERNAL_ERROR.name())
                .message("Something went wrong. Please try again later.")
                .build();
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(body);
    }

}
