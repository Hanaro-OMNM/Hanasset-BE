package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.user.dto.BirthRequest;
import com.omnm.hanasset.user.dto.EmailSignInRequest;
import com.omnm.hanasset.user.dto.EmailSignUpRequest;
import com.omnm.hanasset.user.dto.UserResponse;
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
    public ResponseEntity<UserResponse> logout(HttpServletRequest request) {
        UserResponse userResponse = userService.logout(request);

        if (userResponse.getMessage().equals("ERROR")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserResponse.builder().message("유효하지 않은 요청입니다.").build());
        }

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        Long userId = userDetailsDTO.getId();

        return ResponseEntity.ok().body(UserResponse.builder().message(userDetailsDTO.getUsername()).build());
    }
}
