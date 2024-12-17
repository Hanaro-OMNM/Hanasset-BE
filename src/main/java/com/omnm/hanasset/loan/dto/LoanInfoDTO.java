package com.omnm.hanasset.loan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanInfoDTO {
    private Long loanId;
    private String nameText;
    private String rateText;
    private String limitText;
    private Double dsr;
}
