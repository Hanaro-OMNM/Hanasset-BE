package com.omnm.hanasset.user.utils;

import com.omnm.hanasset.user.dto.UserPropertyResponse;
import com.omnm.hanasset.user.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    @Mapping(source = "user.name", target="name")
    @Mapping(source = "stressDsr", target="dsr")
    UserPropertyResponse propertyToUserPropertyResponse(Property property);
}
