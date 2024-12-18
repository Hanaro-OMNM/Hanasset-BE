package com.omnm.hanasset.realEstate.controller;

import com.omnm.hanasset.global.common.ApiResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateBasicResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateDetailResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateTypeResponse;
import com.omnm.hanasset.realEstate.dto.RealEstatesResponse;
import com.omnm.hanasset.realEstate.service.RealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/real-estates")
public class RealEstateController {
    private final RealEstateService realEstateService;

    @GetMapping
    public ApiResponse<RealEstatesResponse> getRealEstates(@RequestParam Long housingComplexId) {
        RealEstatesResponse realEstates = realEstateService.getRealEstates(housingComplexId);
        return ApiResponse.ok(realEstates);
    }

    @GetMapping("/{realEstateId}/detail")
    public ApiResponse<RealEstateDetailResponse> getRealEstateDetail(@PathVariable Long realEstateId) {
        RealEstateDetailResponse realEstate = realEstateService.getRealEstateDetail(realEstateId);
        return ApiResponse.ok(realEstate);
    }

    @GetMapping("/{realEstateId}/type")
    public ApiResponse<RealEstateTypeResponse> getRealEstateType(@PathVariable Long realEstateId) {
        RealEstateTypeResponse realEstateType = realEstateService.getRealEstateType(realEstateId);
        return ApiResponse.ok(realEstateType);
    }

    @GetMapping("/{realEstateId}/basic")
    public ApiResponse<RealEstateBasicResponse> getRealEstateBasic(@PathVariable Long realEstateId) {
        RealEstateBasicResponse realEstateBasic = realEstateService.getRealEstateBasic(realEstateId);
        return ApiResponse.ok(realEstateBasic);
    }
}

