package com.omnm.hanasset.global.config.security;

import com.omnm.hanasset.consultant.service.ConsultantAuthenticationService;
import com.omnm.hanasset.user.service.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@RequiredArgsConstructor
@Configuration
public class UserDetailsServiceConfig {

    private final UserAuthenticationService userAuthenticationService;
    private final ConsultantAuthenticationService consultantAuthenticationService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
