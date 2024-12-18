package com.omnm.hanasset.socialLogin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor //역직렬화를 위한 기본 생성자
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponse {
    //회원 번호
    @JsonProperty("id")
    public Long id;
    //서비스에 연결 완료된 시각. UTC
    @JsonProperty("connected_at")
    public Date connectedAt;
}
