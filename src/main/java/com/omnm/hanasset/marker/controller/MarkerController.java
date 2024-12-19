package com.omnm.hanasset.marker.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.marker.dto.AptMarkersResponse;
import com.omnm.hanasset.marker.dto.AreaMarkersResponse;
import com.omnm.hanasset.marker.service.MarkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/markers")
public class MarkerController {
    private final MarkerService markerService;

    @GetMapping("/area")
    public ApiResponseEntity<AreaMarkersResponse> getAreaMarkers(@RequestParam int zoom, @RequestParam double lat, @RequestParam double lng) {
        AreaMarkersResponse areaMarkersResponse = markerService.getAreaMarkers(zoom, lat, lng);
        return ApiResponseEntity.ok("시/구/법정동 클러스터 조회 성공", areaMarkersResponse);
    }

    @GetMapping("/apt")
    public ApiResponseEntity<AptMarkersResponse> getAptMarkers(@RequestParam int zoom, @RequestParam double lat, @RequestParam double lng) {
        AptMarkersResponse aptMarkersResponse = markerService.getAptMarkers(zoom, lat, lng);
        return ApiResponseEntity.ok("아파트 단지 클러스터 조회 성공", aptMarkersResponse);
    }
}