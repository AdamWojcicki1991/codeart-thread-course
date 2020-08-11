package com.codeart.thread.week3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LatchesLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        CountDownLatch latch = new CountDownLatch(3);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("[" + Thread.currentThread().getName() + "] Starting run...");
                try {
                    latch.countDown();
                    System.out.println("[" + Thread.currentThread().getName() + "] Waiting for other Threads: " + latch.getCount());
                    latch.await(10, TimeUnit.MINUTES);
                    System.out.println("[" + Thread.currentThread().getName() + "] Working hard...");
                    Thread.sleep(1000);
                    System.out.println("[" + Thread.currentThread().getName() + "] Finishing...");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t0.start();
        Thread.sleep(1000);
        t1.start();
        Thread.sleep(1000);
        t2.start();

        System.out.println("All Thread started");

        System.out.println("DONE");
    }
}
