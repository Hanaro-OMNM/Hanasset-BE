package com.omnm.hanasset.realEstate.service;

import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.global.exception.CustomException;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import com.omnm.hanasset.realEstate.dto.*;
import com.omnm.hanasset.realEstate.entity.RealEstate;
import com.omnm.hanasset.realEstate.repository.RealEstateRepository;
import com.omnm.hanasset.realEstate.utils.RealEstateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RealEstateService {
    private final RealEstateRepository realEstateRepository;
    private final RealEstateMapper realEstateMapper;

    public RealEstatesResponse getRealEstates(UserDetailsDTO userDetailsDTO, Long housingComplexId) {
        List<RealEstate> realEstates = realEstateRepository.findByHousingType_HousingComplex_HousingComplexId(housingComplexId);
        List<RealEstateBookmarkDto> realEstateDtoList = realEstates.stream()
                .map(realEstate -> realEstateMapper.toRealEstateBookmarkDto(realEstate, isBookmarked(userDetailsDTO, realEstate)))
                .collect(Collectors.toList());

        return RealEstatesResponse.builder()
                .count(realEstateDtoList.size())
                .realEstates(realEstateDtoList)
                .build();
    }

    public RealEstateMarketPriceResponse getRealEstateMarketPrice(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new CustomException(ErrorCode.REAL_ESTATE_NOT_FOUND));
        return realEstateMapper.toRealEstateMarketPriceResponse(realEstate);
    }

    public RealEstateBasicResponse getRealEstateBasic(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new CustomException(ErrorCode.REAL_ESTATE_NOT_FOUND));
        return realEstateMapper.toRealEstateBasicResponse(realEstate.getHousingType().getHousingComplex());
    }

    public RealEstateTypeResponse getRealEstateType(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new CustomException(ErrorCode.REAL_ESTATE_NOT_FOUND));
        return realEstateMapper.toRealEstateTypeResponse(realEstate.getHousingType());
    }

    public RealEstateDetailResponse getRealEstateDetail(Long realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new CustomException(ErrorCode.REAL_ESTATE_NOT_FOUND));
        return realEstateMapper.toRealEstateDetailResponse(realEstate);
    }

    public RealEstatesResponse getRecentVisitedRealEstates(UserDetailsDTO userDetailsDTO, List<Long> realEstateIds) {
        List<RealEstate> realEstates = realEstateRepository.findByRealEstateIdIn(realEstateIds);
        List<RealEstateBookmarkDto> realEstateDtoList = realEstates.stream()
                .map(realEstate -> realEstateMapper.toRealEstateBookmarkDto(realEstate, isBookmarked(userDetailsDTO, realEstate)))
                .collect(Collectors.toList());

        return RealEstatesResponse.builder()
                .count(realEstateDtoList.size())
                .realEstates(realEstateDtoList)
                .build();
    }

    private Boolean isBookmarked(UserDetailsDTO userDetailsDTO, RealEstate realEstate) {
        if (userDetailsDTO.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_GUEST"))) {
            return false;
        }
        return realEstate.getBookmarkRealEstates().stream()
                .anyMatch(bookmarkRealEstate -> bookmarkRealEstate.getUser().getUserId().equals(userDetailsDTO.getId()));
    }
}
