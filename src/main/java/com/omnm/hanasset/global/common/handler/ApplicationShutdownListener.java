package com.omnm.hanasset.global.common.handler;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationShutdownListener {

    @EventListener
    public void onApplicationShutdown(ContextClosedEvent event) {
        System.out.println("애플리케이션 종료: 자원 정리 중...");
    }
}
