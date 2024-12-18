package com.omnm.hanasset.socialLogin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverLogoutResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("result")
    private String result;
}
