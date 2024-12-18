package com.omnm.hanasset.global.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        @JsonIgnore HttpStatus httpStatus,
        @Nullable String message,
        @Nullable T result
        ) {
    public static <T> ApiResponse<T> ok(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.OK, null, data);
    }

    public static <T> ApiResponse<T> fail(final ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getHttpStatus(), errorCode.getMessage(), null);
    }

}
