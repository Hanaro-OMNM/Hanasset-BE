package com.omnm.hanasset.loan.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoanRecommendDTO {
    /**
     * TODO RealEstateDTO 추가
     * RealEstateDTO realEstateDTO;
     */
    List<LoanInfoDTO> hanaLoans;
    List<LoanInfoDTO> beotimmokLoans;
}
