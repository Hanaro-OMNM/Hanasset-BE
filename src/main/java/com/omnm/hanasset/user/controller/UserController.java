package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import com.omnm.hanasset.user.dto.*;
import com.omnm.hanasset.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponseEntity signup(
            @RequestBody @Valid EmailSignUpRequest emailSignUpRequest, HttpServletResponse response) throws IOException {

        userService.signUp(emailSignUpRequest);

        return ApiResponseEntity.ok("이메일 회원가입 성공", null);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserResponse> signin(
        @RequestBody @Valid EmailSignInRequest emailSignInRequest, HttpServletResponse response
    ) {
        List<String> tokensList = userService.signIn(emailSignInRequest);

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

        return ResponseEntity.ok().headers(headers).body(UserResponse.builder().message("일반 로그인 성공").build());
    }

    @PostMapping("/birth")
    public ApiResponseEntity birth(@RequestBody @Valid BirthRequest birthRequest) {
        userService.setBirthDate(birthRequest);

        return ApiResponseEntity.ok("생년월일 입력 성공", null);
    }

    @PostMapping("/logout")
    public ApiResponseEntity logout(HttpServletRequest request) {
        String logoutResult = userService.logout(request);

        if (logoutResult.equals("ERROR")) {
            return ApiResponseEntity.fail(ErrorCode.BAD_REQUEST);
        }

        return ApiResponseEntity.ok(logoutResult, null);
    }

    @GetMapping("/me")
    public ApiResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        Long userId = userDetailsDTO.getId();
        UserInfoResponse userInfo = userService.getUserInfo(userId);

        return ApiResponseEntity.ok("회원 정보 조회 성공", userInfo);
    }
}
