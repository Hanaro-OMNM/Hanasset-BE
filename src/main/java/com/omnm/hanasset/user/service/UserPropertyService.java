package com.omnm.hanasset.user.service;

import com.omnm.hanasset.global.exception.CustomException;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import com.omnm.hanasset.user.dto.UserPropertyResponse;
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
    public UserPropertyResponse getUserPropertyInfo (Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Property property = propertyRepository.findByUser_UserId(user.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.PROPERTY_NOT_FOUND));

        return UserPropertyResponse.builder()
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
}
