package com.dunghsk.flockpost.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/**
 * Wrapper chuẩn cho mọi response thành công, để FE luôn nhận được cấu trúc nhất quán:
 *   { "success": true, "data": {...}, "timestamp": "..." }
 *
 * Dùng static factory thay vì new + builder mỗi lần, ví dụ trong Controller:
 *   return ApiResponse.success(userResponse);
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String message;

    @Builder.Default
    private final Instant timestamp = Instant.now();

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder().success(true).data(data).message(message).build();
    }

    public static <T> ApiResponse<T> message(String message) {
        return ApiResponse.<T>builder().success(true).message(message).build();
    }
}
