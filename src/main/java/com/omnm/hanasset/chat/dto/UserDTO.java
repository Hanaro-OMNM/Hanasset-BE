package com.omnm.hanasset.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String name;
    private String birthDate; // datetime
    private String email;
    private String password;
    private String createdAt; // datetime
    private String modifiedAt; // datetime
}