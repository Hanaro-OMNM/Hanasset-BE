package com.omnm.hanasset.global.common.handler;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseStatusHandler implements ResponseBodyAdvice<ApiResponseEntity<?>> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getParameterType() == ApiResponseEntity.class;
    }

    @Override
    public ApiResponseEntity<?> beforeBodyWrite(
            ApiResponseEntity body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        HttpStatus status = body.httpStatus();
        response.setStatusCode(status);

        return body;
    }
}
