package com.omnm.hanasset.consultant.controller;

import com.omnm.hanasset.global.dto.ConsultantDetailsDTO;
import com.omnm.hanasset.consultant.dto.ConsultantInfoResponse;
import com.omnm.hanasset.consultant.dto.ConsultantResponse;
import com.omnm.hanasset.consultant.dto.ConsultantSignInRequest;
import com.omnm.hanasset.consultant.service.ConsultantService;
import com.omnm.hanasset.global.common.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "상담사", description = "상담사 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/consultant")
public class ConsultantController {
    private final ConsultantService consultantService;

    @Operation(summary = "상담사 로그인", description = "상담사 Id와 Password로 로그인한다.")
    @ApiResponse(responseCode = "200", description = "상담사 로그인 성공")
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

    @Operation(summary = "상담사 정보 조회", description = "상담사의 DB Id와 상담사의 이름을 조회한다.")
    @ApiResponse(responseCode = "200", description = "상담사 정보 조회 성공")
    @GetMapping
    public ApiResponseEntity<ConsultantInfoResponse> getConsultantInfo (@AuthenticationPrincipal ConsultantDetailsDTO consultantDetailsDTO) {
        ConsultantInfoResponse consultantInfoResponse = consultantService.getConsultantInfo(consultantDetailsDTO.getUsername());

        return ApiResponseEntity.ok("상담사 정보 조회 성공", consultantInfoResponse);
    }
}
