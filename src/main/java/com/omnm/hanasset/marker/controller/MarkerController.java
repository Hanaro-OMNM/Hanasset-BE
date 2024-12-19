package com.omnm.hanasset.marker.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.marker.dto.AptMarkersResponse;
import com.omnm.hanasset.marker.dto.AreaMarkersResponse;
import com.omnm.hanasset.marker.service.MarkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마커 [클러스터링]", description = "마커 API 목록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/markers")
public class MarkerController {
    private final MarkerService markerService;

    @Operation(summary = "시/구/법정동 클러스터 조회", description = "줌 레벨에 따라 중심 위치를 기준으로 시/구/법정동 클러스터를 조회한다.")
    @ApiResponse(responseCode = "200", description = "시/구/법정동 클러스터 조회 성공")
    @GetMapping("/area")
    public ApiResponseEntity<AreaMarkersResponse> getAreaMarkers(@RequestParam int zoom, @RequestParam double lat, @RequestParam double lng) {
        AreaMarkersResponse areaMarkersResponse = markerService.getAreaMarkers(zoom, lat, lng);
        return ApiResponseEntity.ok("시/구/법정동 클러스터 조회 성공", areaMarkersResponse);
    }

    @Operation(summary = "아파트 단지 클러스터 조회", description = "줌 레벨에 따라 중심 위치를 기준으로 아파트 단지 클러스터를 조회한다.")
    @ApiResponse(responseCode = "200", description = "아파트 단지 클러스터 조회 성공")
    @GetMapping("/apt")
    public ApiResponseEntity<AptMarkersResponse> getAptMarkers(@RequestParam int zoom, @RequestParam double lat, @RequestParam double lng) {
        AptMarkersResponse aptMarkersResponse = markerService.getAptMarkers(zoom, lat, lng);
        return ApiResponseEntity.ok("아파트 단지 클러스터 조회 성공", aptMarkersResponse);
    }
}