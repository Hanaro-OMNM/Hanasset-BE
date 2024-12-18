package com.omnm.hanasset.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisHandler {

    private final RedisConfig redisConfig; // redisConfig.redisTemplate() 여기서 가져오는 redisTemplate은 RedisTemplate<String, Object>

    private final RedisTemplate<String, String> redisStringTemplate;

    /**
     * 리스트에 접근하여 다양한 연산을 수행
     *
     * @return ListOperations<String, Object>
     */
    public ListOperations<String, Object> getListOperations() {
        return redisConfig.redisTemplate().opsForList();
    }

    /**
     * 단일 데이터에 접근하여 다양한 연산을 수행
     *
     * @return ValueOperations<String, Object>
     */
    public ValueOperations<String, Object> getValueOperations() {
        return redisConfig.redisTemplate().opsForValue();
    }

    public String getValue(String key) {
        return redisStringTemplate.opsForValue().get(key);
    }

    @Transactional
    public void setValueOperations(String key, String value, Duration duration){
        redisConfig.redisTemplate().opsForValue().set(key, value, duration);
    }


    /**
     * Redis 작업중 등록, 수정, 삭제에 대해서 처리 및 예외처리를 수행
     *
     * @param operation
     * @return
     */
    public int executeOperation(Runnable operation) {
        try {
            operation.run();
            return 1;
        } catch (Exception e) {
            System.out.println("Redis 작업 오류 발생 :: " + e.getMessage());
            return 0;
        }
    }

    /*
        레디스의 key(지금은 token 이 key) 자체는 true 또는 false 로 가능하지만
        redisTemplate 가 없을 경우 nullPointException 이 발생할 수 있어
        Boolean.TRUE.equals 를 이용해 ture 일 경우 ture,
        false 또는 null 일 경우 false 를 반환하게 만듦
     */

    public boolean keyExists(String token) {
        return Boolean.TRUE.equals(redisConfig.redisTemplate().hasKey(token));
    }
}
