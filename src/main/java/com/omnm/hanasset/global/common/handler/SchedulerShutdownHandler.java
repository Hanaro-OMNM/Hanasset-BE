package com.omnm.hanasset.global.common.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerShutdownHandler implements DisposableBean {

    private final TaskScheduler taskScheduler;

    @Override
    public void destroy() throws Exception {
        if (taskScheduler instanceof ThreadPoolTaskScheduler) {
            ((ThreadPoolTaskScheduler) taskScheduler).shutdown();
            System.out.println("스케줄러가 안전하게 종료되었습니다.");
        }
    }
}
