package com.omnm.hanasset.realEstate.repository;

import com.omnm.hanasset.realEstate.entity.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    List<RealEstate> findByHousingType_HousingComplex_HousingComplexId(Long housingComplexId);
}
