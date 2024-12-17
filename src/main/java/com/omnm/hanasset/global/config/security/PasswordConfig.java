package com.omnm.hanasset.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    // BCrypt 알고리즘으로 변경, 라운드 설정 (솔트 방식 설정은 Spring Security 5.0 부터 가능하여 못 바꿈)
    @Bean
    public PasswordEncoder passwordEncoder() {
        int strength = 12;
        return new BCryptPasswordEncoder(strength);
    }
}
