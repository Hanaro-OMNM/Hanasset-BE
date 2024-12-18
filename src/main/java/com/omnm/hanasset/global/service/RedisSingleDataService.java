package com.omnm.hanasset.global.service;

import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Redis 단일 데이터를 처리하는 비즈니스 로직 인터페이스입니다.
 */
@Service
public interface RedisSingleDataService {
    int setSingleData(String key, Object value);                        // Redis 단일 데이터 값을 등록/수정.
    int setSingleData(String key, Object value, Duration duration);     // Redis 단일 데이터 값을 등록/수정.(duration 값이 존재하면 메모리 상 유효시간을 지정)
    String getSingleData(String key);                                   // Redis 키를 기반으로 단일 데이터의 값을 조회.
    int deleteSingleData(String key);                                   // Redis 키를 기반으로 단일 데이터의 값을 삭제
}