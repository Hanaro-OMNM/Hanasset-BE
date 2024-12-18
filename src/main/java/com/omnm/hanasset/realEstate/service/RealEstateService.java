package com.omnm.hanasset.realEstate.service;

import com.omnm.hanasset.realEstate.dto.*;
import com.omnm.hanasset.realEstate.entity.RealEstate;
import com.omnm.hanasset.realEstate.repository.RealEstateRepository;
import com.omnm.hanasset.realEstate.utils.RealEstateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealEstateService {
    private final RealEstateRepository realEstateRepository;
    private final RealEstateMapper realEstateMapper;

    public RealEstatesResponse getRealEstates(Long housingComplexId) {
        List<RealEstate> realEstates = realEstateRepository.findByHousingType_HousingComplex_HousingComplexId(housingComplexId);
        List<RealEstateDto> realEstateDtoList = realEstates.stream()
                .map(realEstateMapper::toRealEstateDto)
                .collect(Collectors.toList());
        return RealEstatesResponse.builder()
                .count(realEstateDtoList.size())
                .realEstates(realEstateDtoList)
                .build();
    }

    public RealEstateDetailResponse getRealEstateDetail(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부동산이 존재하지 않습니다."));
        return realEstateMapper.toRealEstateDetailResponse(realEstate);
    }

    public RealEstateTypeResponse getRealEstateType(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부동산이 존재하지 않습니다."));
        return realEstateMapper.toRealEstateTypeResponse(realEstate.getHousingType());
    }

    public RealEstateBasicResponse getRealEstateBasic(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부동산이 존재하지 않습니다."));
        return realEstateMapper.toRealEstateBasicResponse(realEstate.getHousingType().getHousingComplex());
    }
}
