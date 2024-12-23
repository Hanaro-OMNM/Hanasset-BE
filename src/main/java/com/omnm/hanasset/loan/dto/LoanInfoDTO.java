package com.omnm.hanasset.loan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanInfoDTO {
    private Long loanId;
    private String name;
    private String rate;
    private String limit;
    private String dsr;
}
