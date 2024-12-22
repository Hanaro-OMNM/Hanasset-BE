package com.omnm.hanasset.loan.controller;

import com.omnm.hanasset.loan.dto.LoanDetailDTO;
import com.omnm.hanasset.loan.dto.LoanRequest;
import com.omnm.hanasset.loan.dto.LoanResponse;
import com.omnm.hanasset.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {
    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<LoanResponse> getRecommendLoans(@RequestParam List<Long> realEstateIds) {
        // userId = 1L
        LoanResponse recommendLoans = loanService.getRecommendLoans(1L, realEstateIds);
        return ResponseEntity.ok(recommendLoans);
    }

}