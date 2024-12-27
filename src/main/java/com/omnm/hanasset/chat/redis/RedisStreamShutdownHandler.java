package com.omnm.hanasset.chat.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisStreamShutdownHandler implements DisposableBean {

    private final RedisConnectionFactory connectionFactory;

    @Override
    public void destroy() throws Exception {
        if (connectionFactory instanceof LettuceConnectionFactory) {
            ((LettuceConnectionFactory) connectionFactory).destroy();
        }
        System.out.println("Redis 연결이 안전하게 종료되었습니다.");
    }
}

