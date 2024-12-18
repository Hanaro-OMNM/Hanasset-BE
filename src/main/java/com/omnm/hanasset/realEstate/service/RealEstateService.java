package com.omnm.hanasset.realEstate.service;

import com.omnm.hanasset.realEstate.dto.RealEstateDetailResponse;
import com.omnm.hanasset.realEstate.entity.RealEstate;
import com.omnm.hanasset.realEstate.repository.RealEstateRepository;
import com.omnm.hanasset.realEstate.utils.RealEstateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealEstateService {
    private final RealEstateRepository realEstateRepository;
    private final RealEstateMapper realEstateMapper;

    public RealEstateDetailResponse getRealEstateDetail(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부동산이 존재하지 않습니다."));
        return realEstateMapper.toRealEstateDetailResponse(realEstate);
    }
}
