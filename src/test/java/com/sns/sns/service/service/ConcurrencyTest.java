package com.sns.sns.service.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConcurrencyTest {


    @Test
    public void test() throws InterruptedException {
        int threadNum = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        for (int i = 0; i < threadNum; i++) {
            executorService.submit(new WorkerThread(countDownLatch));
        }

        executorService.shutdown();
        countDownLatch.await();

        System.out.println("done waiting");

    }
}