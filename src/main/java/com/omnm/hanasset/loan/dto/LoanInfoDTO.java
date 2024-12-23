package com.omnm.hanasset.loan.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanInfoDTO {
    private Long loanId;
    private String name;
    private String rate;
    private String limit;
    private String dsr;
}
