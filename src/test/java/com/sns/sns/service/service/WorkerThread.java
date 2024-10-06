package com.sns.sns.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class WorkerThread implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(WorkerThread.class);
    CountDownLatch countDownLatch;

    WorkerThread(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        log.info("task started");
        countDownLatch.countDown();
    }
}
