package com.omnm.hanasset.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "PROPERTY")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "income")
    private Integer income;

    @Column(name = "capital")
    private Integer capital;

    @Column(name = "hasHouse")
    private Boolean hasHouse;

    @Column(name = "annual_interest")
    private Integer annualInterest;

    @Column(name = "annual_principle")
    private Integer annualPrinciple;

    @Column(name = "is_abnormal_house")
    private Boolean isAbnormalHouse;

    @Column(name = "is_housing_fraud_victim")
    private Boolean isHousingFraudVictim;

    @Column(name = "stress_dsr")
    private Double stressDsr;
}
