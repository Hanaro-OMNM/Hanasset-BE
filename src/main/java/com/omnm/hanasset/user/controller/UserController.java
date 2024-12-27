package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import com.omnm.hanasset.user.dto.*;
import com.omnm.hanasset.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "손님", description = "손님 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "이메일 회원가입", description = "유저의 이메일을 이용해 회원가입을 시도한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 회원가입된 이메일입니다.")
    })
    @PostMapping("/signup")
    public ApiResponseEntity<Void> signup(
            @RequestBody @Valid EmailSignUpRequest emailSignUpRequest, HttpServletResponse response) throws IOException {

        userService.signUp(emailSignUpRequest);

        return ApiResponseEntity.ok("이메일 회원가입 성공", null);
    }

    @Operation(summary = "일반 로그인", description = "유저의 이메일을 이용해 로그인을 시도한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일반 로그인 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "400", description = "비밀번호가 틀립니다.")
    })
    @PostMapping("/signin")
    public ResponseEntity<UserResponse<?>> signin(
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

    @Operation(summary = "생년월일 정보 입력", description = "유저의 생년월일 정보 입력을 시도한다.")
    @ApiResponse(responseCode = "200", description = "생년월일 입력 성공")
    @PostMapping("/birth")
    public ApiResponseEntity<Void> birth(@RequestBody @Valid BirthRequest birthRequest) {
        userService.setBirthDate(birthRequest);

        return ApiResponseEntity.ok("생년월일 입력 성공", null);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 시도한다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ApiResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String logoutResult = userService.logout(request);
        Cookie refreshTokenCookie = new Cookie("refreshToken", null); // 값 설정 없이 null로 초기화
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        if (logoutResult.equals("ERROR")) {
            return ApiResponseEntity.fail(ErrorCode.BAD_REQUEST);
        }

        return ApiResponseEntity.ok(logoutResult, null);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 시도한다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    @DeleteMapping("/withdraw")
    public ApiResponseEntity<Void> withdrawUser(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        Long userId = userDetailsDTO.getId();
        userService.withdrawUser(userId);

        return ApiResponseEntity.ok("회원 탈퇴 성공", null);
    }

    @Operation(summary = "회원 정보 불러오기", description = "유저의 기본 정보 조회를 시도한다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공")
    @GetMapping("/me")
    public ApiResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        Long userId = userDetailsDTO.getId();
        UserInfoResponse userInfo = userService.getUserInfo(userId);

        return ApiResponseEntity.ok("회원 정보 조회 성공", userInfo);
    }

    @Operation(summary = "회원 정보 수정하기", description = "유저의 이름 및 비밀번호 수정을 시도한다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    @PutMapping("/me")
    public ApiResponseEntity<Void> updateUserInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @RequestBody @Valid UserInfoRequest userInfoRequest) {
        Long userId = userDetailsDTO.getId();
        userService.updateUserInfo(userId, userInfoRequest);

        return ApiResponseEntity.ok("회원 정보 수정 성공", null);
    }
}
