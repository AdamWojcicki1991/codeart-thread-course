package com.codeart.thread.week3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarriersLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + "] Waiting for other: " + cyclicBarrier.getNumberWaiting());
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName() + "] Doing heavy work...");
                    Thread.sleep(200);
                    System.out.println(Thread.currentThread().getName() + "] Job done, go home...");
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        Thread t4 = new Thread(task);
        Thread t5 = new Thread(task);
        Thread t6 = new Thread(task);
        Thread t7 = new Thread(task);
        Thread t8 = new Thread(task);
        Thread t9 = new Thread(task);

        t0.start();
        Thread.sleep(250);
        t1.start();
        Thread.sleep(250);
        t2.start();
        Thread.sleep(250);
        t3.start();
        Thread.sleep(250);
        t4.start();

        Thread.sleep(250);

        t5.start();
        Thread.sleep(250);
        t6.start();
        Thread.sleep(250);
        t7.start();
        Thread.sleep(250);
        t8.start();
        Thread.sleep(250);
        t9.start();

        System.out.println("DONE");
    }
}
