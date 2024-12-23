package com.omnm.hanasset.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultantDTO {
    private Long consultantId;
    private String consultantName;
    private String profileImgUrl;
}
