package com.omnm.hanasset.realEstate.repository;

import com.omnm.hanasset.realEstate.entity.HousingComplex;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HousingComplexRepository extends JpaRepository<HousingComplex, Long> {

    @Query("SELECT hc FROM HousingComplex hc WHERE ST_Contains(ST_Buffer(:center, :radius), hc.coordinate)")
    List<HousingComplex> findAllWithinCircle(@Param("center") Point center, @Param("radius") double radius);
}
