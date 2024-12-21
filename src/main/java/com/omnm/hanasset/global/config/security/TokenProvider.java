package com.omnm.hanasset.global.config.security;

import com.omnm.hanasset.global.config.RedisHandler;
import com.omnm.hanasset.user.service.UserAuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.expiration_time}")
    private Long TOKEN_EXPIRE_TIME;

    @Value("${spring.jwt.token.refresh_expiration_time}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    private final UserAuthenticationService userAuthenticationService;

    private final RedisHandler redisHandler;

    // 액세스 토큰 생성
    public String generateAccessToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date(); // 현재 시간
        Date expiredDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
        Duration expiredDuration = Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME); // Duration으로 생성

        String refreshToken = Jwts.builder()
//                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();

        redisHandler.setValueOperations(username, refreshToken, expiredDuration); // Redis 저장

        return refreshToken;
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails =
                this.userAuthenticationService.loadUserByUsername(this.getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        Claims claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().
                    setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // request 헤더에서 token 가져오기
    public String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            return null;
        }
    }

    // 토큰의 남은 시간 가져오기 (토큰 blacklist Redis 저장 때 duration 지정 위해 쓰일 예정)
    public long calculateRemainingTime(Date expiration) {
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }
}
