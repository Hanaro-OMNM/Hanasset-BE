package com.omnm.hanasset.loan.entity;

import jakarta.persistence.Column;
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

    @Column(length = 2048)
    private String typeText;

    @Column(length = 2048)
    private String featureText;

    @Column(length = 2048)
    private String outlineText;

    @Column(length = 2048)
    private String targetGuestText;

    @Column(length = 2048)
    private String targetHouseText;

    @Column(length = 2048)
    private String limitText;

    private String rateText;

    @Column(length = 2048)
    private String periodText;

    @Column(length = 2048)
    private String paybackMethodText;

    private String provider;

    private String rent_type;

    private Double rate;

    private Integer limitAmount;

    private Integer income;

    private Boolean hasHouse;

    private String jobType;

    private Integer maxAge;

    private Integer maxPeriod;

    private Integer maxPrice;

    private Integer minDeposit;

    private Integer maxDeposit;

    private Double exclusiveArea;

    private String paybackMethod;

    private String additionalCondition;
}
