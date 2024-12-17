package com.omnm.hanasset.global.config.security;

import com.omnm.hanasset.global.config.RedisHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 접근할 때마다 검사하는 필터

    private final TokenProvider tokenProvider;
    private final RedisHandler redisHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = tokenProvider.resolveTokenFromRequest(request); // request 헤더에서 토큰 가져오기

        if (StringUtils.hasText(token)) {
            if (tokenProvider.validateToken(token) && ! redisHandler.keyExists(token)) {  // 유효성 검사 + 블랙리스트 확인
                Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth); // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            } else {
                handleInvalidToken(response,
                        // 토큰의 유효기간이 남아있다면 로그아웃 상태, 남아있지 않다면 재로그인이 필요한 상태라고 안내
                        tokenProvider.validateToken(token) ? "이미 로그아웃 상태" : "재로그인 필요한 상태");
                return;  // 에러 처리 후 리턴
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘어가기
    }

    private void handleInvalidToken(HttpServletResponse response, String errorMessage)
            throws IOException {
        log.error("에러 메시지: {}", errorMessage);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(errorMessage);
    }
}
