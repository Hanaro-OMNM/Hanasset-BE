package com.omnm.hanasset.realEstate.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.realEstate.dto.RealEstateBasicResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateDetailResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateTypeResponse;
import com.omnm.hanasset.realEstate.dto.RealEstatesResponse;
import com.omnm.hanasset.realEstate.service.RealEstateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "부동산 매물", description = "부동산 매물 API 목록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/real-estates")
public class RealEstateController {
    private final RealEstateService realEstateService;

    @Operation(summary = "매물 리스트 조회", description = "해당 단지의 매물 리스트를 조회한다.")
    @ApiResponse(responseCode = "200", description = "매물 리스트 조회 성공")
    @GetMapping
    public ApiResponseEntity<RealEstatesResponse> getRealEstates(@RequestParam Long housingComplexId) {
        RealEstatesResponse realEstates = realEstateService.getRealEstates(housingComplexId);
        return ApiResponseEntity.ok("매물 리스트 조회 성공",realEstates);
    }

    @Operation(summary = "기본 정보 조회", description = "특정 매물의 기본 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "기본 정보 조회 성공")
    @GetMapping("/{realEstateId}/basic")
    public ApiResponseEntity<RealEstateBasicResponse> getRealEstateBasic(@PathVariable Long realEstateId) {
        RealEstateBasicResponse realEstateBasic = realEstateService.getRealEstateBasic(realEstateId);
        return ApiResponseEntity.ok("기본 정보 조회 성공", realEstateBasic);
    }

    @Operation(summary = "타입 정보 조회", description = "특정 매물의 타입 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "타입 정보 조회 성공")
    @GetMapping("/{realEstateId}/type")
    public ApiResponseEntity<RealEstateTypeResponse> getRealEstateType(@PathVariable Long realEstateId) {
        RealEstateTypeResponse realEstateType = realEstateService.getRealEstateType(realEstateId);
        return ApiResponseEntity.ok("타입 정보 조회 성공", realEstateType);
    }

    @Operation(summary = "상세 정보 조회", description = "특정 매물의 상세 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "상세 정보 조회 성공")
    @GetMapping("/{realEstateId}/detail")
    public ApiResponseEntity<RealEstateDetailResponse> getRealEstateDetail(@PathVariable Long realEstateId) {
        RealEstateDetailResponse realEstate = realEstateService.getRealEstateDetail(realEstateId);
        return ApiResponseEntity.ok("상세 정보 조회 성공", realEstate);
    }
}

