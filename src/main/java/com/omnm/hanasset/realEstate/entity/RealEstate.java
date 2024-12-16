package com.omnm.hanasset.realEstate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "real_estate")
public class RealEstate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_id")
    private Long realEstateId;

    @NotNull
    @Column(name = "code")
    private Long code;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "rent_type")
    private String rentType;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "coordinate", columnDefinition = "POINT")
    private Point coordinate;

    @Column(name = "deposit")
    private Integer deposit;

    @Column(name = "price")
    private Integer price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "direction_standard")
    private String directionStandard;

    @Column(name = "direction_facing", columnDefinition = "VARCHAR(10)")
    private String directionFacing;

    @Column(name = "total_floor")
    private Integer totalFloor;

    @Column(name = "target_floor")
    private Integer targetFloor;

    @Column(name = "img_urls", columnDefinition = "TEXT")
    private String imgUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "housing_type_id", nullable = false)
    private HousingType housingType;
}
