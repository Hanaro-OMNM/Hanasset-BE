package com.omnm.hanasset.realEstate.entity;

import com.omnm.hanasset.areaCode.entity.AreaCode;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "housing_complex")
public class HousingComplex {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "housing_complex_id")
    private Long housingComplexId;

    @Column(name = "code")
    private Integer code;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "coordinate", columnDefinition = "POINT")
    private Point coordinate;

    @Column(name = "unit_count")
    private Integer unitCount;

    @Column(name = "established_date")
    private LocalDate establishedDate;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "energy_type")
    private String energyType;

    @Column(name = "parking_count")
    private Integer parkingCount;

    @Column(name = "dong_count")
    private Integer dongCount;

    @Column(name = "floor_area_ratio")
    private Double floorAreaRatio;

    @Column(name = "building_coverage_ratio")
    private Double buildingCoverageRatio;

    @Column(name = "construction_company")
    private String constructionCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_code_id", nullable = false)
    private AreaCode areaCode;

    @OneToMany(mappedBy = "housingComplex")
    private List<HousingType> housingTypes;
}
