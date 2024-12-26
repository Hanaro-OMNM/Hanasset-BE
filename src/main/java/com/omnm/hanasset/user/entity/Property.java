package com.omnm.hanasset.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false) // FK가 되는 USER 테이블의 PK
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

    public void updateJobType(String jobType) {
        this.jobType = jobType;
    }

    public void updateIncome(Integer income) {
        this.income = income;
    }

    public void updateCapital(Integer capital) {
        this.capital = capital;
    }

    public void updateHasHouse(Boolean hasHouse) {
        this.hasHouse = hasHouse;
    }

    public void updateAnnualInterest(Integer annualInterest) {
        this.annualInterest = annualInterest;
    }

    public void updateAnnualPrinciple(Integer annualPrinciple) {
        this.annualPrinciple = annualPrinciple;
    }

    public void updateIsAbnormalHouse(Boolean isAbnormalHouse) {
        this.isAbnormalHouse = isAbnormalHouse;
    }

    public void updateIsHousingFraudVictim(Boolean isHousingFraudVictim) {
        this.isHousingFraudVictim = isHousingFraudVictim;
    }

    public void updateStressDsr() {
        int validIncome = (this.income == null || this.income == 0) ? 1 : this.income;
        this.stressDsr = (double) Math.round((float) (this.annualPrinciple + this.annualInterest) / validIncome * 10000) / 100;
    }
}
