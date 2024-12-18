package com.omnm.hanasset.socialLogin.service;

import com.omnm.hanasset.socialLogin.dto.*;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class NaverLoginService {
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String TOKEN_URL;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String USER_URL;

    private String LOGOUT_URL = "https://nid.naver.com/oauth2.0/token";

    public String getAccessToken(String code, String state) {
        NaverTokenResponse naverTokenResponse = WebClient.create(TOKEN_URL)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", code)
                        .queryParam("state", state)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(NaverTokenResponse.class)
                .block();
        return naverTokenResponse.getAccessToken();
    }

    public String getUserId(String accessToken) {
        NaverUserInfoResponse userInfoResponse = WebClient.create(USER_URL)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(NaverUserInfoResponse.class)
                .block();

        log.info("[ Naver Service ] ResultCode ---> {} ", userInfoResponse.getResultCode());
        log.info("[ Naver Service ] Message ---> {} ", userInfoResponse.getMessage());
        log.info("[ Naver Service ] Auth ID ---> {} ", userInfoResponse.getNaverUserInfo().getId());

        return userInfoResponse.getNaverUserInfo().getId();
    }

    public void logout(String accessToken) {
        NaverLogoutResponse naverLogoutResponse = WebClient.create(LOGOUT_URL)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .queryParam("grant_type", "delete")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("access_token", accessToken)
                        .queryParam("service_provider","NAVER")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(NaverLogoutResponse.class)
                .block();

        log.info("[ Naver Service ] AccessToken ---> {} ", naverLogoutResponse.getAccessToken());
        log.info("[ Naver Service ] Result ---> {} ", naverLogoutResponse.getResult());
    }
}
