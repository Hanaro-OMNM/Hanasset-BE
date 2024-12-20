package com.omnm.hanasset.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// JWT 사용하지 않는 버전 -> 추후 수정 예정

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Hanasset API") // API의 제목
                .description("하나셋 프로젝트의 API 문서입니다.") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}