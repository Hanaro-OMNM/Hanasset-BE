package com.omnm.hanasset.loan.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "name_text")
    private String nameText;

    @Column(name = "type_text", columnDefinition = "TEXT")
    private String typeText;

    @Column(name = "feature_text", columnDefinition = "TEXT")
    private String featureText;

    @Column(name = "outline_text", columnDefinition = "TEXT")
    private String outlineText;

    @Column(name = "target_guest_text", columnDefinition = "TEXT")
    private String targetGuestText;

    @Column(name = "target_house_text", columnDefinition = "TEXT")
    private String targetHouseText;

    @Column(name = "limit_text", columnDefinition = "TEXT")
    private String limitText;

    @Column(name="rate_text")
    private String rateText;

    @Column(name = "period_text", columnDefinition = "TEXT")
    private String periodText;

    @Column(name = "payback_method_text", columnDefinition = "TEXT")
    private String paybackMethodText;

    private String provider;

    @Column(name = "rent_type")
    private String rentType;

    private Double rate;

    @Column(name = "limit_amount")
    private Integer limitAmount;

    private Integer income;

    @Column(name = "has_house")
    private Boolean hasHouse;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(name = "max_period")
    private Integer maxPeriod;

    @Column(name = "max_price")
    private Integer maxPrice;

    @Column(name = "min_deposit")
    private Integer minDeposit;

    @Column(name = "max_deposit")
    private Integer maxDeposit;

    @Column(name = "exclusive_area")
    private Double exclusiveArea;

    @Column(name = "payback_method")
    private String paybackMethod;

    @Column(name = "additional_condition")
    private String additionalCondition;
}
