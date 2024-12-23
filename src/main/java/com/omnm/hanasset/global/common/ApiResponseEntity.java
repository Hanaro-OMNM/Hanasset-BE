package com.omnm.hanasset.global.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public record ApiResponseEntity<T>(
        @JsonIgnore HttpStatus httpStatus,
        @Nullable String message,
        @Nullable T result
        ) {
    public static <T> ApiResponseEntity<T> ok(final String msg, @Nullable final T result) {
        return new ApiResponseEntity<>(HttpStatus.OK, msg, result);
    }

    public static <T> ApiResponseEntity<T> fail(final ErrorCode errorCode) {
        return new ApiResponseEntity<>(errorCode.getHttpStatus(), errorCode.getMessage(), null);
    }

}
