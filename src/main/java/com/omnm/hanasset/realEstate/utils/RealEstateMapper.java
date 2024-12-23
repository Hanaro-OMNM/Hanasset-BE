package com.omnm.hanasset.realEstate.utils;

import com.omnm.hanasset.realEstate.dto.RealEstateBasicResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateDetailResponse;
import com.omnm.hanasset.realEstate.dto.RealEstateDto;
import com.omnm.hanasset.realEstate.dto.RealEstateTypeResponse;
import com.omnm.hanasset.realEstate.entity.HousingComplex;
import com.omnm.hanasset.realEstate.entity.HousingType;
import com.omnm.hanasset.realEstate.entity.RealEstate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RealEstateMapper {
    @Mapping(source = "realEstate.targetFloor", target = "floor")
    RealEstateDto toRealEstateDto(RealEstate realEstate);

    @Mapping(source = "housingType.unitCount", target = "unitCount")
    @Mapping(source = "housingType.entranceType", target = "entranceType")
    @Mapping(source = "totalFloor", target = "floorInfo.total")
    @Mapping(source = "targetFloor", target = "floorInfo.target")
    @Mapping(source = "directionStandard", target = "directionInfo.standard")
    @Mapping(source = "directionFacing", target = "directionInfo.facing")
    RealEstateDetailResponse toRealEstateDetailResponse(RealEstate realEstate);

    RealEstateTypeResponse toRealEstateTypeResponse(HousingType housingType);

    @Mapping(source = "housingComplex.address", target = "address")
    @Mapping(source = "housingComplex.unitCount", target = "unitCount")
    @Mapping(source = "housingComplex.parkingCount", target = "parkingCount")
    @Mapping(source = "housingComplex.establishedDate", target = "establishedDate")
    @Mapping(source = "housingComplex.dongCount", target = "dongCount")
    @Mapping(source = "housingComplex.systemType", target = "heatingAndCoolingInfo.systemType")
    @Mapping(source = "housingComplex.energyType", target = "heatingAndCoolingInfo.energyType")
    @Mapping(source = "housingComplex.floorAreaRatio", target = "buildingRatioInfo.floorAreaRatio")
    @Mapping(source = "housingComplex.buildingCoverageRatio", target = "buildingRatioInfo.buildingCoverageRatio")
    @Mapping(source = "housingComplex.constructionCompany", target = "constructionCompany")
    RealEstateBasicResponse toRealEstateBasicResponse(HousingComplex housingComplex);
}
