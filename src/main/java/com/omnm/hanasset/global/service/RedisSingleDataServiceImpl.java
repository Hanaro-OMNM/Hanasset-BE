package com.omnm.hanasset.global.service;

import com.omnm.hanasset.global.config.RedisConfig;
import com.omnm.hanasset.global.config.RedisHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Redis 단일 데이터를 처리하는 비즈니스 로직 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class RedisSingleDataServiceImpl implements RedisSingleDataService {
    private final RedisConfig redisConfig;
    private final RedisHandler redisHandler;

    /**
     * Redis 단일 데이터 값을 등록/수정
     *
     * @param key   : redis key
     * @param value : redis value
     * @return {int} 성공(1), 실패(0)
     */
    @Override
    public int setSingleData(String key, Object value) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value));
    }

    /**
     * Redis 단일 데이터 값을 등록/수정(duration 값이 존재하면 메모리 상 유효시간을 지정)
     *
     * @param key      : redis key
     * @param value:   : redis value
     * @param duration : redis 값 메모리 상의 유효시간.
     * @return {int} 성공(1), 실패(0)
     */
    @Override
    public int setSingleData(String key, Object value, Duration duration) {
        return redisHandler.executeOperation(() -> redisHandler.getValueOperations().set(key, value, duration));
    }

    /**
     * Redis 키를 기반으로 단일 데이터의 값을 조회
     *
     * @param key : redis key
     * @return {String} redis value 값 반환 or 존재하지 않으면 null 반환
     */
    @Override
    public String getSingleData(String key) {
        if (redisHandler.getValueOperations().get(key) == null) return "";
        return String.valueOf(redisHandler.getValueOperations().get(key));
    }

    /**
     * Redis 키를 기반으로 단일 데이터의 값을 삭제
     *
     * @param key : redis key
     * @return {int} 성공(1), 실패(0)
     */
    @Override
    public int deleteSingleData(String key) {
        return redisHandler.executeOperation(() -> redisConfig.redisTemplate().delete(key));
    }
}
