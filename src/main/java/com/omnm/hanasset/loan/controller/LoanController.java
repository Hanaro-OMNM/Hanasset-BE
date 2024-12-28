package com.omnm.hanasset.loan.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.loan.dto.LoanDetailResponse;
import com.omnm.hanasset.loan.dto.LoanResponse;
import com.omnm.hanasset.loan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "대출 추천", description = "대출 추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {
    private final LoanService loanService;

    @Operation(summary = "대출 추천", description = "손님 정보와 매물 정보로 대출을 추천한다.")
    @ApiResponse(responseCode = "200", description = "대출 상품 추천 성공")
    @GetMapping
    public ApiResponseEntity<LoanResponse> getRecommendLoans(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @RequestParam List<Long> realEstateIds) {
        LoanResponse recommendLoans = loanService.getRecommendLoans(userDetailsDTO.getId(), realEstateIds);
        return ApiResponseEntity.ok("대출 상품 추천 성공", recommendLoans);
    }

    @Operation(summary = "대출 상세보기 조회", description = "대출의 상세정보를 조회한다. 이때 유저 정보를 바탕으로 갱신되는 dsr을 함께 보여준다.")
    @ApiResponse(responseCode = "200", description = "대출 상세보기 성공")
    @GetMapping("/detail/{loanId}")
    public ApiResponseEntity<LoanDetailResponse> getLoan(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @PathVariable("loanId") Long loanId) {
        LoanDetailResponse loan = loanService.getLoan(userDetailsDTO.getId(), loanId);
        return ApiResponseEntity.ok("대출 상세보기 성공", loan);
    }

    @Operation(summary = "상담 시 손님이 요청한 손님 및 추천 대출 정보", description = "손님 정보와 등록한 매물 정보로 상담 시 추천 대출 상품 목록을 보여준다.")
    @ApiResponse(responseCode = "200", description = "상담 시 대출 상품 추천 성공")
    @GetMapping("/consulting")
    public ApiResponseEntity<LoanResponse> getConsultingRecommendLoansByGuest(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @RequestParam String chatroomId) {
        LoanResponse recommendLoans = loanService.getConsultingRecommendLoans(userDetailsDTO.getId(), chatroomId);
        return ApiResponseEntity.ok("상담 대출 상품 추천 성공", recommendLoans);
    }

    @Operation(summary = "상담 시 상담원이 요청한 손님 및 추천 대출 정보", description = "손님 정보와 등록한 매물 정보로 상담 시 추천 대출 상품 목록을 보여준다.")
    @ApiResponse(responseCode = "200", description = "상담 시 대출 상품 추천 성공")
    @GetMapping("/consultant/consulting")
    public ApiResponseEntity<LoanResponse> getConsultingRecommendLoansByConsultant(@RequestParam String chatroomId, @RequestParam Long userId) {
        LoanResponse recommendLoans = loanService.getConsultingRecommendLoans(userId, chatroomId);
        return ApiResponseEntity.ok("상담 손님 정보 조회 성공", recommendLoans);
    }

    @Operation(summary = "상담 시 상담원 대출 상세보기 조회", description = "상담 시 상담원에게 대출의 상세정보를 조회해준다. 이때 유저 정보를 바탕으로 갱신되는 dsr을 함께 보여준다.")
    @ApiResponse(responseCode = "200", description = "대출 상세보기 성공")
    @GetMapping("/consultant/detail/{loanId}")
    public ApiResponseEntity<LoanDetailResponse> getLoanConsultant(@RequestParam Long userId, @PathVariable("loanId") Long loanId) {
        LoanDetailResponse loan = loanService.getLoan(userId, loanId);
        return ApiResponseEntity.ok("대출 상세보기 성공", loan);
    }

}