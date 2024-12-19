package com.omnm.hanasset.marker.service;

import com.omnm.hanasset.areaCode.repository.AreaCodeRepository;
import com.omnm.hanasset.marker.dto.*;
import com.omnm.hanasset.marker.utils.MarkerMapper;
import com.omnm.hanasset.marker.utils.NaverMapUtil;
import com.omnm.hanasset.realEstate.entity.HousingComplex;
import com.omnm.hanasset.realEstate.repository.HousingComplexRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarkerService {
    private final AreaCodeRepository areaCodeRepository;
    private final HousingComplexRepository housingComplexRepository;
    private final MarkerMapper markerMapper;

    private static final int SRID = 4326; // WGS84 SRID

    public AreaMarkersResponse getAreaMarkers(int zoom, double lat, double lng) {
        return new AreaMarkersResponse(
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

        if (zoom <= 10) {
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


    public AptMarkersResponse getAptMarkers(int zoom, double lat, double lng) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);
        Point center = geometryFactory.createPoint(new Coordinate(lng, lat));
        center.setSRID(SRID);

        double radius = 0;
        if (zoom == 16) {
            radius = 1500;
        } else if (zoom == 17) {
            radius = 750;
        }

        List<HousingComplex> housingComplexes = housingComplexRepository.findAllWithinCircle(center, radius);

        List<AptMarkerDto> aptMarkers = housingComplexes.stream()
                .map(markerMapper::toAptMarkerDto)
                .collect(Collectors.toList());

        return new AptMarkersResponse(aptMarkers);
    }
}