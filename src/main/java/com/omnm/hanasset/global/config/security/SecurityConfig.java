package com.omnm.hanasset.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    // http 요청에대한 권한 제한 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
                .csrf(CsrfConfigurer::disable) // CSRF 보호 비활성화
                .formLogin(FormLoginConfigurer::disable) // 폼 로그인 비활성화
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/users/signin/**").permitAll()
                                .requestMatchers("/users/signup/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/error/**").permitAll() // 임시
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                );
        return http.build();
    }
}
