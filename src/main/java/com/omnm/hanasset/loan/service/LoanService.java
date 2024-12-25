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
import com.omnm.hanasset.realEstate.utils.RealEstateMapper;
import com.omnm.hanasset.user.dto.UserPropertyResponse;
import com.omnm.hanasset.user.entity.Property;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.repository.PropertyRepository;
import com.omnm.hanasset.user.repository.UserRepository;
import com.omnm.hanasset.user.utils.PropertyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanService {

    private final LoanMapper loanMapper;
    private final RealEstateMapper realEstateMapper;
    private final PropertyMapper propertyMapper;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final PropertyRepository propertyRepository;
    private final RealEstateRepository realEstateRepository;
    private final HousingTypeRepository housingTypeRepository;

    public LoanResponse getRecommendLoans(Long userId, List<Long> realEstateIds) {
        // Exception 임시 처리
        User user = userRepository.findById(userId).orElseThrow();
        Optional<Property> userProperty = propertyRepository.findByUser_UserId(userId);
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
            List<Loan> availableLoans;
            if (userProperty.isPresent()) {
                availableLoans = loanRepository.findAvailableLoans(
                        user.getBirthDate(),
                        userProperty.get().getIncome(),
                        userProperty.get().getHasHouse(),
                        userProperty.get().getJobType(),
                        userProperty.get().getIsAbnormalHouse(),
                        userProperty.get().getIsHousingFraudVictim(),
                        housingType.getExclusiveAreaSize().intValue(),
                        realEstate.getType(),
                        (int) (realEstate.getDeposit() / 10000),
                        (int) (realEstate.getPrice() / 10000));
            }
            else {
                availableLoans = loanRepository.findAll();
            }

            for (Loan loan : availableLoans) {
                LoanInfoDTO loanInfoDTO = loanMapper.loanToInfoDTO(loan);
                loanInfoDTO.setDsr(getNewDSR(userProperty, loan));
                if (loan.getProvider().equalsIgnoreCase("하나")) {
                    hanaLoans.add(loanInfoDTO);
                } else if (loan.getProvider().equalsIgnoreCase("버팀목")) {
                    beotimmokLoans.add(loanInfoDTO);
                }
            }


            loanRecommendInfoDTOS.add(LoanRecommendInfoDTO.builder()
                    .realEstateInfoResponse(realEstateMapper.toRealEstateInfoResponse(realEstate))
                    .hanaLoans(hanaLoans)
                    .beotimmokLoans(beotimmokLoans)
                    .build());
        }

        UserPropertyResponse userPropertyResponse;
        if (userProperty.isPresent()) {
            userPropertyResponse = propertyMapper.propertyToUserPropertyResponse(userProperty.get());
            userPropertyResponse.setAge(calculateAge(user.getBirthDate()));
        }
        else {
            userPropertyResponse = UserPropertyResponse.builder()
                    .name(user.getName())
                    .age(calculateAge(user.getBirthDate()))
                    .jobType("")
                    .income(0)
                    .capital(0)
                    .hasHouse(false)
                    .annualInterest(0)
                    .annualPrinciple(0)
                    .dsr(0.0)
                    .build();
        }

        return LoanResponse.builder()
                .user(userPropertyResponse)
                .loanRecommendInfos(loanRecommendInfoDTOS)
                .build();
    }

    public LoanDetailResponse getLoan(Long userId, Long loanId) {
        // Exception 임시 처리
        Optional<Property> property = propertyRepository.findByUser_UserId(userId);
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        LoanDetailResponse loanDetailDTO = loanMapper.loanToDetailResponse(loan);
        loanDetailDTO.setDsr(getNewDSR(property, loan));
        return loanDetailDTO;
    }

    private Double getNewDSR(Optional<Property> property, Loan loan) {
        int originalAnnualRepayment = 0;
        if (property.isPresent()) {
            originalAnnualRepayment = property.get().getAnnualPrinciple() + property.get().getAnnualInterest();
        }
        int newAnnualRepayment = getNewAnnualRepayment(loan);
        int period = 1;
        if (property.isPresent()) {
            period = Math.max(1, property.get().getIncome());
        }
        return (double) Math.round((float) (originalAnnualRepayment + newAnnualRepayment) / period * 10000) / 100;
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
        double principle = (double) loan.getLimitAmount() / Math.max(1, loan.getMaxPeriod());
        double rate = loan.getRate() / 100;
        for (int i = 0; i < loan.getMaxPeriod(); i++) {
            double interest = Math.max(0, amount) / loan.getMaxPeriod() * rate;
            totalInterest += interest;
            amount -= principle;
        }
        return (int) (loan.getLimitAmount() + totalInterest) / Math.max(1, loan.getMaxPeriod());
    }

    private Integer getBalloonPaymentAnnualRepayment(Loan loan) {
        Double rate = loan.getRate() / 100;
        Double totalInterest = loan.getLimitAmount() * rate / 12 * loan.getMaxPeriod();
        return (int) (loan.getLimitAmount() + totalInterest) / Math.max(1, loan.getMaxPeriod());
    }

    public int calculateAge(LocalDate birthDate) {
        if ((birthDate != null)) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
        return 0;
    }
}
