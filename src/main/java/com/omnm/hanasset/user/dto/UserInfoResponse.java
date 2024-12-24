package com.omnm.hanasset.user.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserInfoResponse {
    private String name;
    private String email;
    private LocalDate birthDate;
}
