package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.user.service.EmailConfirmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/*
    일반 회원가입 시 이메일 인증 관련 컨트롤러
 */

@Tag(name = "이메일 인증", description = "이메일 인증 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/signup")
public class EmailConfirmController {

    private final EmailConfirmService emailConfirmService;

    @Operation(summary = "이메일 인증 코드 보내기", description = "Query Parameter로 전달된 유저 이메일에 대해 인증 코드 전송을 시도한다.")
    @ApiResponse(responseCode = "200", description = "이메일 인증 보내기 성공")
    @PostMapping("/send-email")
    public ApiResponseEntity<?> sendEmail(@RequestParam String email) {
        emailConfirmService.sendEmail(email);

        return ApiResponseEntity.ok("이메일 인증 보내기 성공", null);
    }

    @Operation(summary = "이메일 인증 완료", description = "유저의 이메일 인증 코드 일치 여부를 확인한다.")
    @ApiResponse(responseCode = "200", description = "이메일 인증 완료 성공")
    @GetMapping("/receive-code")
    public ApiResponseEntity<?> receiveCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code) {

        emailConfirmService.codeConfirm(email, code);

        return ApiResponseEntity.ok("이메일 인증 완료 성공", null);
    }
}
