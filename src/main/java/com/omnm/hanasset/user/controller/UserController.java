package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.user.dto.EmailSignInRequest;
import com.omnm.hanasset.user.dto.EmailSignUpRequest;
import com.omnm.hanasset.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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
        String token = userService.signIn(emailSignInRequest);
        log.info("token : {}", token);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        log.info("로그인 성공, email : {}", emailSignInRequest.getEmail());
        log.info("token : {}", token);
        return ResponseEntity.ok().headers(headers).body("로그인 성공\n이메일 : " + emailSignInRequest.getEmail());
    }

    
}
