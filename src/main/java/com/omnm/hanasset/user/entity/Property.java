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
    private Long propertyId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private String jobType ;

    private Integer income;

    private Integer capital;

    private Boolean hasHouse;

    private Integer annualInterest;

    private Integer annualPrinciple;

    private Boolean isAbnormalHouse;

    private Boolean isHousingFraudVictim;

    private Double stressDsr;
}
