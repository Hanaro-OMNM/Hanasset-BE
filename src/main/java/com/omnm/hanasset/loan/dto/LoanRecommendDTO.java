package com.omnm.hanasset.loan.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecommendDTO {
    /**
     * TODO RealEstateDTO 추가
     * RealEstateDTO realEstateDTO;
     */
    List<LoanInfoDTO> hanaLoans;
    List<LoanInfoDTO> beotimmokLoans;
}
