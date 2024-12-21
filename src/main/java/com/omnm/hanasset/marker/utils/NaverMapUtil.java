package com.omnm.hanasset.marker.utils;

import static java.lang.Math.pow;

public class NaverMapUtil {

    /**
     * 줌 레벨에 따른 반경(km)을 계산합니다.
     *
     * @param zoomLevel 줌 레벨 (네이버 지도 기준)
     * @return 반경(km)
     */
    public static double calculateRadiusByZoomLevel(int zoomLevel) {
        return 750 * pow(2, 17 - zoomLevel);
    }
}