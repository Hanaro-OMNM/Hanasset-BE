package com.omnm.hanasset.global.config.security;

import com.omnm.hanasset.global.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String ERROR_CODE_ATTRIBUTE = "errorCode";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException {

        // 요청 속성에서 ErrorCode 가져오기
        ErrorCode errorCode = (ErrorCode) request.getAttribute(ERROR_CODE_ATTRIBUTE);

        if (errorCode == null) {
            errorCode = ErrorCode.AUTHORIZATION_FAILED; // 기본 ErrorCode 설정
        }

        log.error("에러 메시지: {}", errorCode.getMessage());

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\": \"" + errorCode.getMessage() + "\", \"result\": null}");
    }
}
