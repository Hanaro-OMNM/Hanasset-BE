package com.omnm.hanasset.loan.controller;

import com.omnm.hanasset.loan.dto.LoanRequest;
import com.omnm.hanasset.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    LoanService loanService;

    @GetMapping
    public ResponseEntity<Void> getLoan(LoanRequest loanRequest) {
        loanService.getAvailableLoan(loanRequest);
        return ResponseEntity.ok(null);
    }

}