package com.omnm.hanasset.loan.utils;

import com.omnm.hanasset.loan.dto.LoanDetailResponse;
import com.omnm.hanasset.loan.dto.LoanInfoDTO;
import com.omnm.hanasset.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanMapper {
    @Mapping(source = "nameText", target = "name")
    LoanInfoDTO loanToInfoDTO(Loan loan);

    @Mapping(source = "nameText", target = "name")
    @Mapping(source = "typeText", target = "type")
    @Mapping(source = "outlineText", target = "outline")
    @Mapping(source = "featureText", target = "feature")
    @Mapping(source = "targetGuestText", target = "targetGuest")
    @Mapping(source = "targetHouseText", target = "targetHouse")
    @Mapping(source = "periodText", target = "period")
    @Mapping(source = "paybackMethodText", target = "paybackMethod")
    LoanDetailResponse loanToDetailResponse(Loan loan);
}
