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
        try {
            String accessToken = tokenProvider.resolveTokenFromRequest(request); // request 헤더에서 토큰 가져오기
            String refreshToken = tokenProvider.resolveRefreshTokenFromCookie(request); // request 쿠키에서 토큰 가져오기

            if (StringUtils.hasText(accessToken)) {
                handleAccessToken(accessToken, refreshToken, request, response);
            } else if (StringUtils.hasText(refreshToken)) {
                handleRefreshToken(refreshToken, response);
            }

            filterChain.doFilter(request, response); // 다음 필터로 넘어가기
        } catch (Exception e) {
            log.error("Unexpected error during authentication", e);
            handleInvalidToken(response, "인증 처리 중 오류가 발생했습니다.");
        }
    }

    private void handleAccessToken(String accessToken, String refreshToken, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            if (tokenProvider.validateToken(accessToken)) {
                if (!redisHandler.keyExists(accessToken)) {
                    setAuthentication(accessToken); // 토큰이 유효하고 로그아웃 블랙리스트에도 없을 경우, 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                } else {
                    handleInvalidToken(response, "로그아웃된 토큰입니다.");
                }
            } else if (StringUtils.hasText(refreshToken)) {
                // access token이 유효하지 않을 경우, refresh token 체크
                handleRefreshToken(refreshToken, response);
            }
        } catch (ExpiredJwtException e) {
            log.warn("Access token has expired", e);

            if (StringUtils.hasText(refreshToken)) {
                handleRefreshToken(refreshToken, response);
            } else {
                handleInvalidToken(response, "만료된 액세스 토큰입니다.");
            }
        } catch (Exception e) {
            log.error("Error validating access token", e);
            handleInvalidToken(response, "잘못된 액세스 토큰입니다.");
        }
    }

    private void handleRefreshToken(String refreshToken, HttpServletResponse response) throws IOException {
        try {
            String username = tokenProvider.getUsername(refreshToken);
            String storedUsername = redisHandler.getValue(refreshToken);

            if (username.equals(storedUsername) && tokenProvider.validateToken(refreshToken)) {
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
            redisHandler.deleteByKey(refreshToken);

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

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\": \"" + errorMessage + "\", \"result\": null}");
    }
}
