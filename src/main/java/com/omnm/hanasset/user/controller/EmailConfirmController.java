package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.user.dto.UserResponse;
import com.omnm.hanasset.user.service.EmailConfirmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    일반 회원가입 시 이메일 인증 관련 컨트롤러
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/signup")
public class EmailConfirmController {

    private final EmailConfirmService emailConfirmService;

    @PostMapping("/sendemail")
    public ResponseEntity<UserResponse> sendEmail(@RequestParam String email) {
        emailConfirmService.sendEmail(email);

        return ResponseEntity.ok().body(UserResponse.builder().message("이메일 인증 보내기 성공").build());
    }

    @GetMapping("/receive_code")
    public ResponseEntity<UserResponse> receiveCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code) {

        emailConfirmService.codeConfirm(email, code);

        return ResponseEntity.ok().body(UserResponse.builder().message("이메일 인증 완료 성공").build());
    }
}
