package com.omnm.hanasset.realEstate.controller;

import com.omnm.hanasset.global.common.ApiResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateDetailResponse;
import com.omnm.hanasset.realEstate.service.RealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/real-estates")
public class RealEstateController {
    private final RealEstateService realEstateService;

    @GetMapping
    public ResponseEntity<Object> getRealEstates(@RequestParam Long housingComplexId) {
        return ResponseEntity.ok(housingComplexId);
    }

    @GetMapping("/{realEstateId}/detail")
    public ApiResponse<RealEstateDetailResponse> getRealEstateDetail(@PathVariable Long realEstateId) {
        RealEstateDetailResponse realEstate = realEstateService.getRealEstateDetail(realEstateId);
        return ApiResponse.ok(realEstate);
    }

}
