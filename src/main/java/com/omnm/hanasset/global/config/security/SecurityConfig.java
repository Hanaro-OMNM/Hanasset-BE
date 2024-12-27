package com.omnm.hanasset.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
                .csrf(CsrfConfigurer::disable) // CSRF 보호 비활성화
                .formLogin(FormLoginConfigurer::disable) // 폼 로그인 비활성화; JWT 사용하기 때문
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 비활성화
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/real-estates/**").authenticated()
                                //USER, GUEST 접근 가능 end point
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/markers/**").permitAll()
                                .requestMatchers("/users/signin/**").permitAll()
                                .requestMatchers("/users/signup/**").permitAll()
                                .requestMatchers("/users/birth/**").permitAll()
                                .requestMatchers("/users/logout/**").authenticated()
                                .requestMatchers("/users/withdraw/**").authenticated()
                                .requestMatchers("/users/me/**").authenticated()
                                .requestMatchers("/users/property/**").authenticated()
                                .requestMatchers("/loan/**").authenticated()
                                .requestMatchers("/chat/**").permitAll()
                                .requestMatchers("/ws-chat/**").permitAll()
                                .requestMatchers("/markers/**").permitAll()
                                .requestMatchers("/real-estates/**").permitAll()
                                .requestMatchers("/users/bookmarks/**").authenticated()
                                .requestMatchers("/consultant/signin/**").permitAll()
                                .requestMatchers("/consultant/**").authenticated()
                                .requestMatchers("/swagger", "/swagger-ui.html").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                                .requestMatchers("/error/**", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg").permitAll() // 임시
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                );
        return http.build();
    }

}
