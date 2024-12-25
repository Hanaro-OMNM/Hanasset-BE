package com.omnm.hanasset.realEstate.utils;

import com.omnm.hanasset.realEstate.dto.*;
import com.omnm.hanasset.realEstate.entity.HousingComplex;
import com.omnm.hanasset.realEstate.entity.HousingType;
import com.omnm.hanasset.realEstate.entity.RealEstate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

    @Mapping(source = "housingType.housingComplex.code", target = "complexNumber")
    @Mapping(source = "housingType.code", target = "pyeongTypeNumber")
    @Mapping(source = "type", target = "tradeType", qualifiedByName = "mapTradeType")
    RealEstateMarketPriceResponse toRealEstateMarketPriceResponse(RealEstate realEstate);

    @Mapping(source = "housingType.housingComplex.address", target = "address")
    @Mapping(source = "housingType.exclusiveAreaSize", target = "exclusiveAreaSize")
    RealEstateInfoResponse toRealEstateInfoResponse(RealEstate realEstate);

    @Named("mapTradeType")
    default String mapTradeType(String type) {
        if ("전세".equals(type)) {
            return "B1";
        } else if ("월세".equals(type)) {
            return "B2";
        }
        return type;
    }
}
