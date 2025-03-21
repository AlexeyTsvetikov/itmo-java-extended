package com.example.itmo.extended.jobs;

import com.example.itmo.extended.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {
    private final UserService userService;
    private static int counter = 0;

    @Scheduled(cron = "0 0 3 * * */2")
    public void invalidateSessions() {
        userService.invalidateSessions();
    }

//    @Scheduled(fixedDelay = 3000)
    public void sendMsg(){
        String msg = "test" + counter++ + "@yandex.ru";
        log.info(msg);
    }
}
