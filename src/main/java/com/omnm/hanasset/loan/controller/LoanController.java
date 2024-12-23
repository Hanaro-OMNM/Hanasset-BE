package com.omnm.hanasset.loan.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.loan.dto.LoanDetailResponse;
import com.omnm.hanasset.loan.dto.LoanResponse;
import com.omnm.hanasset.loan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ApiResponseEntity<LoanResponse> getRecommendLoans(@RequestParam List<Long> realEstateIds) {
        // userId = 1L
        LoanResponse recommendLoans = loanService.getRecommendLoans(1L, realEstateIds);
        return ApiResponseEntity.ok("대출 상품 추천 성공", recommendLoans);
    }

    @Operation(summary = "대출 상세보기 조회", description = "대출의 상세정보를 조회한다. 이때 유저 정보를 바탕으로 갱신되는 dsr을 함께 보여준다.")
    @ApiResponse(responseCode = "200", description = "대출 상세보기 성공")
    @GetMapping("/detail/{loanId}")
    public ApiResponseEntity<LoanDetailResponse> getLoan(@PathVariable("loanId") Long loanId) {
        // userId = 1L
        LoanDetailResponse loan = loanService.getLoan(1L, loanId);
        return ApiResponseEntity.ok("대출 상세보기 성공", loan);
    }

}