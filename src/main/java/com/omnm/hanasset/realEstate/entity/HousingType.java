package com.omnm.hanasset.realEstate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "housing_type")
public class HousingType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "housing_type_id")
    private Long housingTypeId;

    @Column(name = "code")
    private Integer code;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_count")
    private Integer unitCount;

    @Column(name = "entrance_type")
    private String entranceType;

    @Column(name = "supply_area_size")
    private Double supplyAreaSize;

    @Column(name = "exclusive_area_size")
    private Double exclusiveAreaSize;

    @Column(name = "management_fee")
    private Double managementFee;

    @Column(name = "room_count")
    private Integer roomCount;

    @Column(name = "bathroom_count")
    private Integer bathroomCount;

    @Column(name = "floor_plan_img_url", columnDefinition = "VARCHAR(2048)")
    private String floorPlanImgUrl;

    @Column(name = "floor_plan_link", columnDefinition = "VARCHAR(2048)")
    private String floorPlanLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "housing_complex_id", nullable = false)
    private HousingComplex housingComplex;

    @OneToMany(mappedBy = "housingType")
    private List<RealEstate> realEstates;
}
