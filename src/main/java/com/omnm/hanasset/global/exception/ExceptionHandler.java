package com.omnm.hanasset.global.exception;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    // 존재하지 않는 요청에 대한 예외
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponseEntity<?> handleNoPageFoundException(Exception e) {
        log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", e.getMessage());
        return ApiResponseEntity.fail(ErrorCode.NOT_FOUND);
    }

    // 커스텀 예외
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {CustomException.class})
    public ApiResponseEntity<?> handleCustomException(CustomException e) {
        log.error("handleCustomException() in GlobalExceptionHandler throw CustomException : {}", e.getMessage());
        return ApiResponseEntity.fail(e.getErrorCode());
    }

    // 기본 예외
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    public ApiResponseEntity<?> handleException(Exception e) {
        log.error("handleException() in GlobalExceptionHandler throw Exception : {}", e.getMessage());
        e.printStackTrace();
        return ApiResponseEntity.fail(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
