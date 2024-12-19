package com.omnm.hanasset.areaCode.repository;

import com.omnm.hanasset.areaCode.entity.AreaCode;
import com.omnm.hanasset.markers.dto.CurrentMarkersDTO;
import com.omnm.hanasset.markers.dto.ShowAreaCodeDTO;


import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AreaCodeRepository extends JpaRepository<AreaCode, Long> {

    @Query("""
    SELECT new com.omnm.hanasset.markers.dto.ShowAreaCodeDTO(co.address, co.code, co.coordinate)
    FROM AreaCode co
    WHERE (co.address LIKE '%별시'
        OR co.address LIKE '%역시'
        OR co.address LIKE '%도')
    AND ST_Contains(ST_Buffer(:center, :radius), co.coordinate)
    """)
    List<ShowAreaCodeDTO> findAllWithinCircleSiDo(@Param("center") Point center,
                                                  @Param("radius") double radius);

    @Query("""
    SELECT new com.omnm.hanasset.markers.dto.ShowAreaCodeDTO(co.address, co.code, co.coordinate)
    FROM AreaCode co
    WHERE (co.address LIKE '%시' AND co.address NOT LIKE '%별시' AND co.address NOT LIKE '%역시'
        OR co.address LIKE '%군'
        OR co.address LIKE '%구')
    AND ST_Contains(ST_Buffer(:center, :radius), co.coordinate)
    """)
    List<ShowAreaCodeDTO> findAllWithinCircleSiGunGu(@Param("center") Point center,
                                                     @Param("radius") double radius);

    @Query("""
    SELECT new com.omnm.hanasset.markers.dto.ShowAreaCodeDTO(co.address, co.code, co.coordinate)
    FROM AreaCode co
    WHERE co.address LIKE '%동'
    AND ST_Contains(ST_Buffer(:center, :radius), co.coordinate)
    """)
    List<ShowAreaCodeDTO> findAllWithinCircleDong(@Param("center") Point center,
                                                  @Param("radius") double radius);

    @Query("""
    SELECT new com.omnm.hanasset.markers.dto.CurrentMarkersDTO(co.code, co.cityName, co.sigunguName, co.emdName)
    FROM AreaCode co
    WHERE (co.address LIKE '%별시'
        OR co.address LIKE '%역시'
        OR co.address LIKE '%도')
    ORDER BY ST_Distance(co.coordinate, :center) ASC
    LIMIT 1
    """)
    CurrentMarkersDTO findNearSiDo(@Param("center") Point center);

    @Query("""
    SELECT new com.omnm.hanasset.markers.dto.CurrentMarkersDTO(co.code, co.cityName, co.sigunguName, co.emdName)
    FROM AreaCode co
    WHERE (co.address LIKE '%시' AND co.address NOT LIKE '%별시' AND co.address NOT LIKE '%역시'
        OR co.address LIKE '%군'
        OR co.address LIKE '%구')
    ORDER BY ST_Distance(co.coordinate, :center) ASC
    LIMIT 1
    """)
    CurrentMarkersDTO findNearSiGunGu(@Param("center") Point center);

    @Query("""
    SELECT new com.omnm.hanasset.markers.dto.CurrentMarkersDTO(co.code, co.cityName, co.sigunguName, co.emdName)
    FROM AreaCode co
    WHERE co.address LIKE '%동'
    ORDER BY ST_Distance(co.coordinate, :center) ASC
    LIMIT 1
    """)
    CurrentMarkersDTO findNearDong(@Param("center") Point center);
}