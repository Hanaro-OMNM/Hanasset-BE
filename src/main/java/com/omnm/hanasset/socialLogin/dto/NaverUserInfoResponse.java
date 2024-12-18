package com.omnm.hanasset.socialLogin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserInfoResponse {
    @JsonProperty("resultcode")
    public String resultCode;

    @JsonProperty("message")
    public String message;

    @JsonProperty("response")
    public NaverUserInfo naverUserInfo;

}
