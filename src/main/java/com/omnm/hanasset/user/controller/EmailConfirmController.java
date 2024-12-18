package com.omnm.hanasset.user.controller;

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
    public ResponseEntity<String> sendEmail(@RequestParam String email) {
        emailConfirmService.sendEmail(email);

        log.info("메일 발송 성공, 이메일 : {}", email);
        return ResponseEntity.ok().body("메일을 확인해 주세요.");
    }

    @GetMapping("/receive_code")
    public ResponseEntity<String> receiveCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code) {

        emailConfirmService.codeConfirm(email, code);

        log.info("인증 완료, 이메일 : {}", email);
        return ResponseEntity.ok().body("이메일 인증 완료 성공");
    }
}
