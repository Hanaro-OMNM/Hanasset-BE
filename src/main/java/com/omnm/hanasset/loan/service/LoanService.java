package com.omnm.hanasset.loan.service;

import com.omnm.hanasset.loan.LoanMapper;
import com.omnm.hanasset.loan.dto.LoanInfoDTO;
import com.omnm.hanasset.loan.dto.LoanRequest;
import com.omnm.hanasset.loan.entity.Loan;
import com.omnm.hanasset.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LoanService {

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    LoanRepository loanRepository;

    public void getAvailableLoan(LoanRequest loanRequest) {
        List<Loan> loans = loanRepository.findAll();

        // userRepository로 유저 가져오기
        // realEstateRepository로 매물 정보 가져오기

        /**
         * todo 필터링 로직
         * 대출을 순회하며
         * 유저: 나이, 연소득, 주택수, 직업, 추가조건(비정상거처, 전세피해인)
         * 매물: 타입, 전용면적, 임차보증금,
         */

        for (Loan loan : loans) {
            LoanInfoDTO loanInfoDTO = loanMapper.loanToDTO(loan);
            System.out.println("loanInfoDTO.getNameText() = " + loanInfoDTO.getNameText());
        }

        // 기존 DSR 계산

        // 갱신 DSR 계산


    }

}
