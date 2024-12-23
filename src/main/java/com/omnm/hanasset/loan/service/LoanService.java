package com.omnm.hanasset.loan.service;

import com.omnm.hanasset.loan.dto.LoanDetailResponse;
import com.omnm.hanasset.loan.dto.LoanInfoDTO;
import com.omnm.hanasset.loan.dto.LoanRecommendInfoDTO;
import com.omnm.hanasset.loan.dto.LoanResponse;
import com.omnm.hanasset.loan.entity.Loan;
import com.omnm.hanasset.loan.repository.LoanRepository;
import com.omnm.hanasset.loan.utils.LoanMapper;
import com.omnm.hanasset.realEstate.entity.HousingType;
import com.omnm.hanasset.realEstate.entity.RealEstate;
import com.omnm.hanasset.realEstate.repository.HousingTypeRepository;
import com.omnm.hanasset.realEstate.repository.RealEstateRepository;
import com.omnm.hanasset.user.entity.Property;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.repository.PropertyRepository;
import com.omnm.hanasset.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanService {

    private final LoanMapper loanMapper;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final PropertyRepository propertyRepository;
    private final RealEstateRepository realEstateRepository;
    private final HousingTypeRepository housingTypeRepository;

    public LoanResponse getRecommendLoans(Long userId, List<Long> realEstateIds) {
        // Exception 임시 처리
        User user = userRepository.findById(userId).orElseThrow();
        Property property = propertyRepository.findByUser_UserId(userId).orElseThrow();
        List<RealEstate> realEstates = realEstateRepository.findAllById(realEstateIds);

        /**
         * 필터링 로직
         * 대출을 순회하며
         * 유저: 나이, 연소득, 주택 보유여부, 직업, 추가조건(비정상거처, 전세피해인)
         * 매물: 타입, 전용면적, 임차보증금, 월세
         * 전세: deposit만 비교
         * 월세: deposit과 price 비교
         */
        List<LoanRecommendInfoDTO> loanRecommendInfoDTOS = new ArrayList<>();

        for (RealEstate realEstate : realEstates) {
            List<LoanInfoDTO> hanaLoans = new ArrayList<>();
            List<LoanInfoDTO> beotimmokLoans = new ArrayList<>();

            HousingType housingType = housingTypeRepository.findById(realEstate.getHousingType().getHousingTypeId()).orElseThrow();
            List<Loan> availableLoans = loanRepository.findAvailableLoans(
                    user.getBirthDate(),
                    property.getIncome(),
                    property.getHasHouse(),
                    property.getJobType(),
                    property.getIsAbnormalHouse(),
                    property.getIsHousingFraudVictim(),
                    housingType.getExclusiveAreaSize().intValue(),
                    realEstate.getType(),
                    (int) (realEstate.getDeposit() / 10000),
                    (int) (realEstate.getPrice() / 10000));

            for (Loan loan : availableLoans) {
                LoanInfoDTO loanInfoDTO = loanMapper.loanToInfoDTO(loan);
                loanInfoDTO.setDsr(String.format("%.2f", getNewDSR(property, loan)));
                if (loan.getProvider().equalsIgnoreCase("하나")) {
                    hanaLoans.add(loanInfoDTO);
                } else if (loan.getProvider().equalsIgnoreCase("버팀목")) {
                    beotimmokLoans.add(loanInfoDTO);
                }
            }

            /**
             * TODO RealEstateDTO 추가 필요
             */
            loanRecommendInfoDTOS.add(LoanRecommendInfoDTO.builder()
                    .hanaLoans(hanaLoans)
                    .beotimmokLoans(beotimmokLoans)
                    .build());
        }

        /**
         * TODO GuestDTO 추가 필요
         */
        return LoanResponse.builder()
                .loanRecommendInfoDTOS(loanRecommendInfoDTOS)
                .build();
    }

    public LoanDetailResponse getLoan(Long userId, Long loanId) {
        // Exception 임시 처리
        Property property = propertyRepository.findByUser_UserId(userId).orElseThrow();
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        LoanDetailResponse loanDetailDTO = loanMapper.loanToDetailResponse(loan);
        loanDetailDTO.setDsr(String.format("%.2f", getNewDSR(property, loan)));
        return loanDetailDTO;
    }

    private Double getNewDSR(Property property, Loan loan) {
        int originalAnnualRepayment = property.getAnnualPrinciple() + property.getAnnualInterest();
        int newAnnualRepayment = getNewAnnualRepayment(loan);
        return (double) (originalAnnualRepayment + newAnnualRepayment) / property.getIncome() * 100;
    }

    private Integer getNewAnnualRepayment(Loan loan) {
        return switch (loan.getPaybackMethod()) {
            case "equalPrinciplePayment" -> getEqualPrinciplePaymentAnnualRepayment(loan);
            case "balloonPayment" -> getBalloonPaymentAnnualRepayment(loan);
            default -> 0;
        };
    }

    private Integer getEqualPrinciplePaymentAnnualRepayment(Loan loan) {
        double totalInterest = 0;
        Double amount = Double.valueOf(loan.getLimitAmount());
        double principle = (double) loan.getLimitAmount() / loan.getMaxPeriod();
        double rate = loan.getRate() / 100;
        for (int i = 0; i < loan.getMaxPeriod(); i++) {
            double interest = Math.max(0, amount) / loan.getMaxPeriod() * rate;
            totalInterest += interest;
            amount -= principle;
        }
        return (int) (loan.getLimitAmount() + totalInterest) / loan.getMaxPeriod();
    }

    private Integer getBalloonPaymentAnnualRepayment(Loan loan) {
        Double rate = loan.getRate() / 100;
        Double totalInterest = loan.getLimitAmount() * rate / 12 * loan.getMaxPeriod();
        return (int) (loan.getLimitAmount() + totalInterest) / loan.getMaxPeriod();
    }
}
