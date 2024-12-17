package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.user.dto.EmailSignUpRequest;
import com.omnm.hanasset.user.service.EmailSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class EmailSignUpController {
    @Autowired
    private EmailSignUpService emailSignUpService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody @Valid EmailSignUpRequest emailSignUpRequest) {
        emailSignUpService.signUp(emailSignUpRequest);

        log.info("회원가입 성공, 이메일 : {}", emailSignUpRequest.getEmail());
        return ResponseEntity.ok("회원가입 완료\n이메일 : " + emailSignUpRequest.getEmail());
    }
}
