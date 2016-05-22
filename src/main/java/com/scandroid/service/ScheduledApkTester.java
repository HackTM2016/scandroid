package com.scandroid.service;

/**
 * Created by mihai on 21/05/16.
 */

import com.scandroid.domain.Application;
import com.scandroid.domain.Link;
import com.scandroid.domain.Scan;
import com.scandroid.repository.ApplicationRepository;
import com.scandroid.repository.LinkRepository;
import com.scandroid.repository.ScanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledApkTester {

    private final Logger log = LoggerFactory.getLogger(ScheduledApkTester.class);

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ScanRepository scanRepository;

    @Inject
    private LinkRepository linkRepository;

    public static final String proxyOutputFilePath = "/nas/web/hack/scandroid/apks/proxy.log";

    private static final Object lock = new Object();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 100000)
    @Transactional
    public void  appsThatNeedScanning() {
        synchronized (lock) {
            List<Application> applicationList = applicationRepository.findAllThatNeedScanning();
            if (applicationList.isEmpty()) {
                return;
            }

            log.debug("Apps that need scanning:");
            for (Application a : applicationRepository.findAllThatNeedScanning()) {
                boolean scan = true;
                if (a.getScans().isEmpty()) {
                    try {
                        log.debug("Scanning:" + a.getPackageName());
                        ProcessBuilder pb = new ProcessBuilder("/nas/web/hack/scandroid/apks/downloadApk.sh", a.getPackageName());
                        //Process process = pb.start();
                       // boolean succes = process.waitFor(120L, TimeUnit.SECONDS);
                        boolean succes=true;
                        log.debug("Command executed, any errors? " + (succes ? "No" : "Yes"));
                        if (!succes) {
                            log.error("Could not scan, setting error!");
                            Scan s = new Scan();
                            s.setApplication(a);
                            s.setSuccess(false);
                            s.setUpdated(LocalDate.now());
                            scanRepository.saveAndFlush(s);
                        } else {
                            //log.debug("Command Output:\n" + output(process.getInputStream()));
                            Scan s = new Scan();

                            //send the apk to adb

                            String command = "/Applications/Genymotion.app/Contents/MacOS/tools/adb " +
                                "install /nas/web/hack/scandroid/apks/" + a.getPackageName() + ".apk";
                            log.debug("Install command: " + command);
                            ProcessBuilder pb2 = new ProcessBuilder("/Applications/Genymotion.app/Contents/MacOS/tools/adb",
                                "install", "/nas/web/hack/scandroid/apks/" + a.getPackageName() + ".apk");

                            Process process = pb2.start();
                            succes = process.waitFor(120L, TimeUnit.SECONDS);
                            log.debug("Will sleep 20. Install command executed, any errors? " + (succes ? "No" : "Yes"));

                            Thread.sleep(10000);



                            String command2 = "/Applications/Genymotion.app/Contents/MacOS/tools/adb" +
                                " shell  monkey -p " + a.getPackageName() + " -v 500";

                            log.debug("Run command:" + command2);
                            ProcessBuilder pb3 = new ProcessBuilder("/Applications/Genymotion.app/Contents/MacOS/tools/adb",
                                "shell", "monkey", "-p", a.getPackageName(), "-v", "500");


                            process = pb3.start();
                            succes = process.waitFor(200L, TimeUnit.SECONDS);
                            log.debug("Will sleep 60s. Monkey test command executed, any errors? " + (succes ? "No" : "Yes"));
                            Thread.sleep(10000);


                            String command3 = "/Applications/Genymotion.app/Contents/MacOS/tools/adb uninstall " + a.getPackageName();
                            ProcessBuilder pb4 = new ProcessBuilder("/Applications/Genymotion.app/Contents/MacOS/tools/adb", "uninstall", a.getPackageName());

                            process = pb4.start();
                            succes = process.waitFor(120L, TimeUnit.SECONDS);
                            log.debug("Uninstall command executed, any errors? " + (succes ? "No" : "Yes"));

                            List<String> proxyData = getProxyData();

                            s.setApplication(a);
                            s.setSuccess(true);
                            s.setUpdated(LocalDate.now());
                            scanRepository.saveAndFlush(s);


                            Set<Link> linkSet= new HashSet<>();
                            for(String data:proxyData) {
                                if(data.contains("https")){
                                    Link currentLink = new Link();
                                    currentLink.setUrl(data);
                                    currentLink.setPostData("");
                                    currentLink.setScan(s);
                                    linkSet.add(currentLink);
                                    linkRepository.saveAndFlush(currentLink);
                                }
                            }
                            s.setLinks(linkSet);
                            scanRepository.saveAndFlush(s);


                         }
                    } catch (IOException e) {
                        log.error("IOException:" + e.toString());
                    } catch (InterruptedException e) {
                        log.error("Interrupted Exception:" + e.toString());
                    }
                }
            }
        }
    }


    private List<String> getProxyData(){
        List<String> data = new LinkedList<>();
        try {
            FileReader in = new FileReader(proxyOutputFilePath);
            BufferedReader br = new BufferedReader(in);

            String line;

            while ( (line = br.readLine()) != null) {
                data.add(line);
            }
            in.close();
        } catch (Exception   e) {
            e.printStackTrace();
        }
        return data;
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
