package com.optimax.pms.automation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HousekeepingScheduler {

    /**
     * Stubbed scheduled job for generating housekeeping tasks.
     * Runs every night at 02:00 server time.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void generateDailyTasks() {
        log.info("HousekeepingScheduler.generateDailyTasks - stub execution");
    }
}

