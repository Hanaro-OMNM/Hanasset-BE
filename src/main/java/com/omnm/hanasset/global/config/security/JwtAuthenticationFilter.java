package com.omnm.hanasset.global.config.security;

import com.omnm.hanasset.global.config.RedisHandler;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

        String accessToken = tokenProvider.resolveTokenFromRequest(request); // request 헤더에서 토큰 가져오기
        String refreshToken = tokenProvider.resolveRefreshTokenFromCookie(request); // request 쿠키에서 토큰 가져오기

        String username = tokenProvider.getUsername(accessToken);

        if (StringUtils.hasText(accessToken)) {
            if (tokenProvider.validateToken(accessToken) && ! redisHandler.keyExists(accessToken)) {  // 유효성 검사 + 블랙리스트 확인
                setAuthentication(accessToken); // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            } else if (StringUtils.hasText(refreshToken)) {
                validateAndRefreshToken(refreshToken, username, response);
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘어가기
    }

    private void validateAndRefreshToken(String refreshToken, String username, HttpServletResponse response) throws IOException {
        try {
            String storedToken = redisHandler.getValue(username);

            if (refreshToken.equals(storedToken) && tokenProvider.validateToken(refreshToken)) {
                // Refresh token이 유효하면 새로운 Access token 발급

                String newAccessToken = tokenProvider.generateAccessToken(username);
                // 새로운 Access token을 헤더에 추가
                response.setHeader("Authorization", "Bearer " + newAccessToken);
                // Authentication 설정
                setAuthentication(newAccessToken);
            }

            else {
                // 유효하지 않거나 일치하지 않는 리프레시 토큰 처리
                logger.error("Refresh token is invalid or expired");
                handleInvalidToken(response, "인증되지 않는 토큰입니다.");
            }
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰 만료 처리
            redisHandler.deleteByKey(username);

            logger.error("Refresh token has expired", e);
            handleInvalidToken(response, "만료된 토큰입니다.");
        } catch (Exception e) {
            // Refresh token 검증 실패 시 로그만 남김
            logger.error("Invalid refresh token", e);
        }
    }

    private void setAuthentication(String token) {
        // 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(token));
    }

    // 토큰 관련 예외 처리
    private void handleInvalidToken(HttpServletResponse response, String errorMessage)
            throws IOException {

        log.error("에러 메시지: {}", errorMessage);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + errorMessage + "\", \"result\": null}");
    }
}
