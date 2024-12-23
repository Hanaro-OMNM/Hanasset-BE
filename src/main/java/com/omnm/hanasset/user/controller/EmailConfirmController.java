package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.user.service.EmailConfirmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponseEntity sendEmail(@RequestParam String email) {
        emailConfirmService.sendEmail(email);

        return ApiResponseEntity.ok("이메일 인증 보내기 성공", null);
    }

    @GetMapping("/receive_code")
    public ApiResponseEntity receiveCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code) {

        emailConfirmService.codeConfirm(email, code);

        return ApiResponseEntity.ok("이메일 인증 완료 성공", null);
    }
}
