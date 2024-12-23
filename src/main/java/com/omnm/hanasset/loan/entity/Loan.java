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

    @Column(name = "type_text", columnDefinition = "VARCHAR(2048)")
    private String typeText;

    @Column(name = "feature_text", columnDefinition = "VARCHAR(2048)")
    private String featureText;

    @Column(name = "outline_text", columnDefinition = "VARCHAR(2048)")
    private String outlineText;

    @Column(name = "target_guest_text", columnDefinition = "VARCHAR(2048)")
    private String targetGuestText;

    @Column(name = "target_house_text", columnDefinition = "VARCHAR(2048)")
    private String targetHouseText;

    @Column(name = "limit_text", columnDefinition = "VARCHAR(2048)")
    private String limitText;

    private String rateText;

    @Column(name = "period_text", columnDefinition = "VARCHAR(2048)")
    private String periodText;

    @Column(name = "payback_method_text", columnDefinition = "VARCHAR(2048)")
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
