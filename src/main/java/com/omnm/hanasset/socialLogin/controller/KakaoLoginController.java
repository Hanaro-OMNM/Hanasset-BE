package com.omnm.hanasset.socialLogin.controller;

import com.omnm.hanasset.socialLogin.dto.KakaoUserInfoResponse;
import com.omnm.hanasset.socialLogin.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<Void> callback(@RequestParam("code") String code) {
        System.out.println("code = " + code);

        String accessToken = kakaoLoginService.getAccessToken(code);
        System.out.println("accessToken = " + accessToken);

        KakaoUserInfoResponse userInfo = kakaoLoginService.getUserInfo(accessToken);

        Long id = userInfo.getId();
        System.out.println("id = " + id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
