package com.omnm.hanasset.areaCode.entity;


import com.omnm.hanasset.realEstate.entity.HousingComplex;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "area_code")
public class AreaCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_code_id")
    private Long areaCodeId;

    @NotNull
    @Column(name = "code")
    private Long code;

    @NotNull
    @Column(name = "address")
    private String address;

    @Column(name = "city_code")
    private Long cityCode;

    @Column(name = "sigungu_code")
    private Long sigunguCode;

    @Column(name = "emd_code")
    private Long emdCode;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "sigungu_name")
    private String sigunguName;

    @Column(name = "emd_name")
    private String emdName;

    @NotNull
    @Column(name = "coordinate")
    private Point coordinate;

    @OneToMany(mappedBy = "areaCode")
    private List<HousingComplex> housingComplexes;

}
