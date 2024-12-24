package com.omnm.hanasset.consultant.controller;

import com.omnm.hanasset.consultant.dto.ConsultantResponse;
import com.omnm.hanasset.consultant.dto.ConsultantSignInRequest;
import com.omnm.hanasset.consultant.service.ConsultantService;
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

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/consultant")
public class ConsultantController {
    private final ConsultantService consultantService;

    @PostMapping("/signin")
    public ResponseEntity<ConsultantResponse<?>> signin(
            @RequestBody @Valid ConsultantSignInRequest consultantSignInRequest, HttpServletResponse response) throws IOException {

        List<String> tokensList = consultantService.signIn(consultantSignInRequest);

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

        return ResponseEntity.ok().headers(headers).body(ConsultantResponse.builder().message("상담사 로그인 성공").build());
    }
}
