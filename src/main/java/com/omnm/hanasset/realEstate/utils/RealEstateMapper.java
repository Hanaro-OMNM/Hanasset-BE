package com.omnm.hanasset.realEstate.utils;

import com.omnm.hanasset.realEstate.dto.RealEstateDetailResponse;
import com.omnm.hanasset.realEstate.entity.RealEstate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RealEstateMapper {
    @Mapping(source = "housingType.unitCount", target = "unitCount")
    @Mapping(source = "housingType.entranceType", target = "entranceType")
    @Mapping(source = "totalFloor", target = "floorInfo.total")
    @Mapping(source = "targetFloor", target = "floorInfo.target")
    @Mapping(source = "directionStandard", target = "directionInfo.standard")
    @Mapping(source = "directionFacing", target = "directionInfo.facing")
    RealEstateDetailResponse toRealEstateDetailResponse(RealEstate realEstate);


}
