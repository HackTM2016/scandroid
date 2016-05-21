package com.scandroid.service;

/**
 * Created by mihai on 21/05/16.
 */
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledApkTester {

    private final Logger log = LoggerFactory.getLogger(ScheduledApkTester.class);


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.error("The time is now " + dateFormat.format(new Date()));
    }
}
