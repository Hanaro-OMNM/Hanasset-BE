package com.omnm.hanasset.markers.service;

import com.omnm.hanasset.areaCode.repository.AreaCodeRepository;
import com.omnm.hanasset.markers.dto.*;
import com.omnm.hanasset.markers.utils.NaverMapUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarkersAreaService {
    private final AreaCodeRepository areaCodeRepository;
    private static final int SRID = 4326; // WGS84 SRID

    public MarkersAreaResponse getMarkersArea(int zoom, double lat, double lng) {
        return new MarkersAreaResponse(
                getCurrentMarkersArea(zoom, lat, lng), getShowMarkersList(zoom, lat, lng));
    }

    public CurrentMarkersDTO getCurrentMarkersArea(int zoom, double lat, double lng) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);
        Point center = geometryFactory.createPoint(new Coordinate(lng, lat));
        center.setSRID(SRID);
        CurrentMarkersDTO result;
        if (zoom <= 9) {
            result = areaCodeRepository.findNearSiDo(center);
        } else if (zoom <= 13) {
            result = areaCodeRepository.findNearSiGunGu(center);
        } else {
            result = areaCodeRepository.findNearDong(center);
        }

        return result;
    }

    public List<ShowMarkersDTO> getShowMarkersList(int zoom, double lat, double lng) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);
        Point center = geometryFactory.createPoint(new Coordinate(lng, lat));
        center.setSRID(SRID);
        double radius = NaverMapUtil.calculateRadiusByZoomLevel(zoom);

        List<ShowAreaCodeDTO> result;

        if (zoom <= 9) {
            result = areaCodeRepository.findAllWithinCircleSiDo(center, radius);
        } else if (zoom <= 13) {
            result = areaCodeRepository.findAllWithinCircleSiGunGu(center, radius);
        } else {
            result = areaCodeRepository.findAllWithinCircleDong(center, radius);
        }

        System.out.println(zoom + ' ' + radius);

        return result.stream()
                .map(this::convertToMarkersAreaDTO)
                .collect(Collectors.toList());
    }

    private ShowMarkersDTO convertToMarkersAreaDTO(ShowAreaCodeDTO areaCodeDTO) {
        double latitude = areaCodeDTO.getCoordinate().getY();
        double longitude = areaCodeDTO.getCoordinate().getX();

        return new ShowMarkersDTO(
                areaCodeDTO.getName(),
                areaCodeDTO.getCode(),
                latitude,
                longitude
        );
    }

}