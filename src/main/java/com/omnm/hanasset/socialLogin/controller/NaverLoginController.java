package com.omnm.hanasset.socialLogin.controller;

import com.omnm.hanasset.socialLogin.service.NaverLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

@RestController
@RequiredArgsConstructor
public class NaverLoginController {

    private final NaverLoginService naverLoginService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    String REDIRECT_URI;

    @GetMapping("/login/naver")
    public ResponseEntity<Void> naverLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + clientId + "&state=" + generateState() + "&redirect_uri=" + REDIRECT_URI);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login/oauth2/code/naver")
    public ResponseEntity<Void> successCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        String accessToken = naverLoginService.getAccessToken(code, state);
        String userId = naverLoginService.getUserId(accessToken);
        /**
         * TODO
         * DB에 소셜 제공자 이름, 소셜 제공자 고유 id
         */
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout/naver")
    public ResponseEntity<Void> logout(@RequestParam("accessToken") String accessToken) {
        /**
         * TODO
         * logout test
         * accessToken을 헤더에서 받아 꺼내도록 변경
         */
        naverLoginService.logout(accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String generateState() {
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        return state;
    }

}
