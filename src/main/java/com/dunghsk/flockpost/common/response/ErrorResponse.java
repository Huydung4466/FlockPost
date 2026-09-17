package com.dunghsk.flockpost.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;

/**
 * Cấu trúc lỗi chuẩn trả về cho FE:
 *   { "success": false, "errorCode": "RESOURCE_NOT_FOUND", "message": "...", "timestamp": "..." }
 * fieldErrors chỉ xuất hiện khi lỗi validate (danh sách field nào sai, sai gì).
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final boolean success;
    private final String errorCode;
    private final String message;
    private final List<FieldError> fieldErrors;

    @Builder.Default
    private final Instant timestamp = Instant.now();

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
    }
}
