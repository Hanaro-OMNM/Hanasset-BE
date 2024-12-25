package com.omnm.hanasset.user.service;

import com.omnm.hanasset.global.exception.CustomException;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import com.omnm.hanasset.user.dto.PropertyResponse;
import com.omnm.hanasset.user.entity.Property;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.repository.PropertyRepository;
import com.omnm.hanasset.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserPropertyService {

    private final UserRepository userRepository;

    private final PropertyRepository propertyRepository;

    @Transactional
    public PropertyResponse getUserPropertyInfo (Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Property property = propertyRepository.findByUser_UserId(user.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.PROPERTY_NOT_FOUND));

        return PropertyResponse.builder()
                    .jobType(property.getJobType())
                    .income(property.getIncome())
                    .capital(property.getCapital())
                    .hasHouse(property.getHasHouse())
                    .annualInterest(property.getAnnualInterest())
                    .annualPrincipal(property.getAnnualPrinciple())
                    .isAbnormalHouse(property.getIsAbnormalHouse())
                    .isHousingFraudVictim(property.getIsHousingFraudVictim())
                    .stressDsr(property.getStressDsr())
                .build();
    }

    @Transactional
    public void updateUserPropertyInfo (Long userId, String type, String value) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Property property = propertyRepository.findByUser_UserId(user.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.PROPERTY_NOT_FOUND));

        if (type.equals("jobType")) {
            property.updateJobType(value);
        } else if (type.equals("income")) {
            Integer income = Integer.valueOf(value);
            property.updateIncome(income);
        } else if (type.equals("capital")) {
            Integer capital = Integer.valueOf(value);
            property.updateCapital(capital);
        } else if (type.equals("hasHouse")) {
            Boolean hasHouse = Boolean.valueOf(value);
            property.updateHasHouse(hasHouse);
        } else if (type.equals("annualInterest")) {
            Integer annualInterest = Integer.valueOf(value);
            property.updateAnnualInterest(annualInterest);
            property.updateStressDsr();
        } else if (type.equals("annualPrinciple")) {
            Integer annualPrinciple = Integer.valueOf(value);
            property.updateAnnualPrinciple(annualPrinciple);
            property.updateStressDsr();
        } else if (type.equals("isAbnormalHouse")) {
            Boolean isAbnormalHouse = Boolean.valueOf(value);
            property.updateIsAbnormalHouse(isAbnormalHouse);
        } else if (type.equals("isHousingFraudVictim")) {
            Boolean isHousingFraudVictim = Boolean.valueOf(value);
            property.updateIsHousingFraudVictim(isHousingFraudVictim);
        }

    }
}
