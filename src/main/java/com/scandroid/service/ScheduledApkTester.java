package com.scandroid.service;

/**
 * Created by mihai on 21/05/16.
 */

import com.scandroid.domain.Application;
import com.scandroid.domain.Scan;
import com.scandroid.repository.ApplicationRepository;
import com.scandroid.repository.ScanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledApkTester {

    private final Logger log = LoggerFactory.getLogger(ScheduledApkTester.class);

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ScanRepository scanRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void appsThatNeedScanning() {
        List<Application> applicationList = applicationRepository.findAllThatNeedScanning();
        if(applicationList.isEmpty()){
            return;
        }

        log.debug("Apps that need scanning:");
        for(Application a: applicationRepository.findAllThatNeedScanning()){
            boolean scan = true;
            if(a.getScans().isEmpty()){
                try {
                    log.debug("Scanning:"+a.getPackageName());
                    ProcessBuilder pb = new ProcessBuilder("/nas/web/hack/scandroid/apks/downloadApk.sh", a.getPackageName());
                    Process process = pb.start();
                    boolean succes = process.waitFor(120L, TimeUnit.SECONDS);
                    log.debug("Command executed, any errors? " + (succes ? "No" : "Yes"));
                    if(!succes){
                        log.error("Could not scan, setting error!");
                        Scan s = new Scan();
                        s.setApplication(a);
                        s.setSuccess(false);
                        s.setUpdated(LocalDate.now());
                        scanRepository.saveAndFlush(s);
                    }else {
                        log.debug("Command Output:\n" + output(process.getInputStream()));
                        Scan s = new Scan();
                        s.setApplication(a);
                        s.setSuccess(true);
                        s.setUpdated(LocalDate.now());
                        scanRepository.saveAndFlush(s);
                    }
                } catch (IOException e) {
                    log.error("IOException:"+e.toString());
                } catch (InterruptedException e) {
                    log.error("Interrupted Exception:"+e.toString());
                }
            }
        }

    }

    private String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }
}
