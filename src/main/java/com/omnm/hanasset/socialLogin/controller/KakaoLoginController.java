package com.omnm.hanasset.socialLogin.controller;

import com.omnm.hanasset.socialLogin.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String REDIRECT_URI;

    @GetMapping("/login/kakao")
    public ResponseEntity<Void> kakaoLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + REDIRECT_URI);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<Void> callback(@RequestParam("code") String code) {
        String accessToken = kakaoLoginService.getAccessToken(code);
        String userId = kakaoLoginService.getUserId(accessToken);
        /**
         * TODO
         * DB에 소셜 제공자 이름, 소셜 제공자 고유 id
         */
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout/kakao")
    public ResponseEntity<Void> logout(@RequestParam("accessToken") String accessToken) {
        /**
         * TODO
         * logout test
         * accessToken을 헤더에서 받아 꺼내도록 변경
         */
        kakaoLoginService.logout(accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
