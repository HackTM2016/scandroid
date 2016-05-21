package com.scandroid.service;

/**
 * Created by mihai on 21/05/16.
 */

import com.scandroid.domain.Application;
import com.scandroid.domain.Scan;
import com.scandroid.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.SimpleDateFormat;

@Component
public class ScheduledApkTester {

    private final Logger log = LoggerFactory.getLogger(ScheduledApkTester.class);

    @Inject
    private ApplicationRepository applicationRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 10000)
    public void appsThatNeedScanning() {
        log.debug("Apps that need scanning:");
        for(Application a: applicationRepository.findAllThatNeedScanning()){
            boolean scan = true;
            if(!a.getScans().isEmpty()){
                for(Scan s: a.getScans()){
                    if(s.isSuccess()){
                        scan = false;
                        break;
                    }
                }
            }
            if(scan){
                log.debug("Scanning:"+a.getPackageName());
            }
        }

    }
}
