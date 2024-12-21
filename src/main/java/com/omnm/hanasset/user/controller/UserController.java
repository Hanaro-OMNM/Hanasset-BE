package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.user.dto.EmailSignInRequest;
import com.omnm.hanasset.user.dto.EmailSignUpRequest;
import com.omnm.hanasset.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final HttpServletResponse response;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody @Valid EmailSignUpRequest emailSignUpRequest) {
        userService.signUp(emailSignUpRequest);

        log.info("이메일 회원가입 성공, 이메일 : {}", emailSignUpRequest.getEmail());
        return ResponseEntity.ok("이메일 회원가입 성공\n이메일 : " + emailSignUpRequest.getEmail());
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(
        @RequestBody @Valid EmailSignInRequest emailSignInRequest
    ) {
        List<String> tokensList = userService.signIn(emailSignInRequest);
        log.info("access token : {}", tokensList.get(0));

        // Access Token은 헤더에 저장
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokensList.get(0));

        // Refresh Token은 쿠키에 저장
        Cookie refreshCookie = new Cookie("refreshToken", tokensList.get(1));
        refreshCookie.setHttpOnly(true); // JavaScript를 통한 접근 방지
        refreshCookie.setSecure(true); // HTTPS를 통해서만 쿠키 전송
        refreshCookie.setPath("/"); // 사이트 전체에서 쿠키 사용
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 1주
        response.addCookie(refreshCookie);

        log.info("로그인 성공, email : {}", emailSignInRequest.getEmail());

        return ResponseEntity.ok().headers(headers).body("로그인 성공\n이메일 : " + emailSignInRequest.getEmail());
    }


}
