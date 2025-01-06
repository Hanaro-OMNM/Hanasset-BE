package com.omnm.hanasset.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:3000", "https://www.om-nm.com", "https://consultant.om-nm.com") // 허용할 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 필요한 HTTP 메서드 추가
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Authorization", "Refresh-Token") // 노출할 헤더
                .allowCredentials(true) // 쿠키 및 인증 정보 허용
                .maxAge(3600); // preflight 요청 캐싱 시간 (초 단위)
    }
}
