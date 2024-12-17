package com.omnm.hanasset.loan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Loan {
    @Id
    private Long loanId;
    private String nameText;
    private String typeText;
    private String featureText;
    private String outlineText;
    private String targetGuestText;
    private String targetHouseText;
    private String limitText;
    private String rateText;
    private String periodText;
    private String paybackMethodText;
    private String type;
    private Integer income;
    private Integer numHouse;
    private String jobType;
    private Integer exclusiveArea;
    private Integer maxPrice;
    private Integer minDeposit;
    private Integer maxDeposit;
    private Integer maxLimit;
    private Integer maxPeriod;
    private Double rate;
    private String additionalCondition;
}
