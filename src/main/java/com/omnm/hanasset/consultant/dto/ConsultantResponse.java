package com.omnm.hanasset.consultant.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConsultantResponse<T> {
    private String message;
    private T result;
}
