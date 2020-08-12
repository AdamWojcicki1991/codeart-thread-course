package com.codeart.thread.week4;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPollsLesson {
    public static void main(String[] args) {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        ExecutorService executor = Executors.newCachedThreadPool();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "-" + LocalDateTime.now().toString() + "] Doing hard work...");
                    Thread.sleep(200);
                    System.out.println("[" + Thread.currentThread().getName() + "-" + LocalDateTime.now().toString() + "] Job done, go home...");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

//        scheduledExecutorService.schedule(task, 500, TimeUnit.MILLISECONDS);
//        scheduledExecutorService.scheduleAtFixedRate(task, 0, 500, TimeUnit.MILLISECONDS);
        scheduledExecutorService.scheduleWithFixedDelay(task, 200, 500, TimeUnit.MILLISECONDS);

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }
}
