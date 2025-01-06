package com.omnm.hanasset.marker.utils;

import com.omnm.hanasset.marker.dto.AptMarkerDto;
import com.omnm.hanasset.realEstate.entity.HousingComplex;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MarkerMapper {
    @Mapping(source = "housingComplexId", target = "housingComplexId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "coordinate.y", target = "centerLat")
    @Mapping(source = "coordinate.x", target = "centerLng")
    AptMarkerDto toAptMarkerDto(HousingComplex housingComplex);

}
