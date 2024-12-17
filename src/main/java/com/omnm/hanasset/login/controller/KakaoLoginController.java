package com.omnm.hanasset.login.controller;

import com.omnm.hanasset.login.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<Void> callback(@RequestParam("code") String code) {
        System.out.println("code = " + code);

        String accessToken = kakaoLoginService.getAccessToken(code);
        System.out.println("accessToken = " + accessToken);

        Map<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);

        String id = (String) userInfo.get("id");
        System.out.println("id = " + id);

//        String email = (String) userInfo.get("email");
//        String nickname = (String) userInfo.get("nickname");
//        System.out.println("email = " + email);
//        System.out.println("nickname = " + nickname);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
