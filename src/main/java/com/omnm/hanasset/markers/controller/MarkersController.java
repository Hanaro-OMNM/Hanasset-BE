package com.omnm.hanasset.markers.controller;

import com.omnm.hanasset.global.common.ApiResponse;
import com.omnm.hanasset.markers.dto.MarkersAreaResponse;
import com.omnm.hanasset.markers.service.MarkersAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/markers")
public class MarkersController {
    @Autowired
    private MarkersAreaService markersAreaService;

    @GetMapping("/area")
    public ApiResponse<MarkersAreaResponse> getAreaMarkers(@RequestParam int zoom, @RequestParam double lat, @RequestParam double lng) {
        MarkersAreaResponse markersAreaResponse = markersAreaService.getMarkersArea(zoom, lat, lng);
        return ApiResponse.ok("시/구/법정동 클러스터 조회 성공", markersAreaResponse);
    }
}