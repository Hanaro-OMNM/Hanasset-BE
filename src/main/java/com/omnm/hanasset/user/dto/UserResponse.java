package com.omnm.hanasset.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse<T> {
    private String message;
    private T result;
}
