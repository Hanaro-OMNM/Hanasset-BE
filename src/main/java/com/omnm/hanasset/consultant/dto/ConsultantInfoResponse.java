package com.omnm.hanasset.consultant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConsultantInfoResponse {
    private Long consultantId;
    private String name;
}
