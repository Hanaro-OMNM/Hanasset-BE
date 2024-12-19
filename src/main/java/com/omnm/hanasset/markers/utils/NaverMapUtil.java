package com.omnm.hanasset.markers.utils;

public class NaverMapUtil {

    // 지구 둘레 (km 단위)
    private static final double EARTH_CIRCUMFERENCE = 40075.0;

    // 타일 크기 (네이버 지도: 기본 256x256 픽셀)
    private static final int TILE_SIZE = 256;

    /**
     * 줌 레벨에 따른 반경(km)을 계산합니다.
     *
     * @param zoomLevel 줌 레벨 (네이버 지도 기준)
     * @return 반경(km)
     */
    public static double calculateRadiusByZoomLevel(int zoomLevel) {
        if (zoomLevel < 0) throw new IllegalArgumentException("Zoom level cannot be negative");

        // 줌 레벨에 따른 1픽셀당 거리 (km)
        double distancePerPixel = EARTH_CIRCUMFERENCE / (TILE_SIZE * Math.pow(2, zoomLevel));

        // 지도 화면의 반지름을 256 픽셀 기준으로 가정 (중심에서 반경으로 계산)
        double radiusInKm = distancePerPixel * (TILE_SIZE / 2) * 1000;

        return radiusInKm;
    }
}