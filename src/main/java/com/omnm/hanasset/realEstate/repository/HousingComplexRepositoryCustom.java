package com.omnm.hanasset.realEstate.repository;

import com.omnm.hanasset.realEstate.entity.HousingComplex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface HousingComplexRepositoryCustom {
    Page<HousingComplex> searchWithKeyword(PageRequest pageRequest, String keyword);
}
