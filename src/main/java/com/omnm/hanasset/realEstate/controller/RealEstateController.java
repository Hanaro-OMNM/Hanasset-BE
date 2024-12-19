package com.omnm.hanasset.realEstate.controller;

import com.omnm.hanasset.global.common.ApiResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateBasicResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateDetailResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateTypeResponse;
import com.omnm.hanasset.realEstate.dto.RealEstatesResponse;
import com.omnm.hanasset.realEstate.service.RealEstateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "부동산 매물", description = "부동산 매물 API 목록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/real-estates")
public class RealEstateController {
    private final RealEstateService realEstateService;

    @GetMapping
    public ApiResponse<RealEstatesResponse> getRealEstates(@RequestParam Long housingComplexId) {
        RealEstatesResponse realEstates = realEstateService.getRealEstates(housingComplexId);
        return ApiResponse.ok("매물 리스트 조회 성공",realEstates);
    }

    @GetMapping("/{realEstateId}/detail")
    public ApiResponse<RealEstateDetailResponse> getRealEstateDetail(@PathVariable Long realEstateId) {
        RealEstateDetailResponse realEstate = realEstateService.getRealEstateDetail(realEstateId);
        return ApiResponse.ok("매물 정보 조회 성공", realEstate);
    }

    @GetMapping("/{realEstateId}/type")
    public ApiResponse<RealEstateTypeResponse> getRealEstateType(@PathVariable Long realEstateId) {
        RealEstateTypeResponse realEstateType = realEstateService.getRealEstateType(realEstateId);
        return ApiResponse.ok("매물 타입 조회 성공", realEstateType);
    }

    @GetMapping("/{realEstateId}/basic")
    public ApiResponse<RealEstateBasicResponse> getRealEstateBasic(@PathVariable Long realEstateId) {
        RealEstateBasicResponse realEstateBasic = realEstateService.getRealEstateBasic(realEstateId);
        return ApiResponse.ok("매물 기본정보 조회 성공", realEstateBasic);
    }
}

