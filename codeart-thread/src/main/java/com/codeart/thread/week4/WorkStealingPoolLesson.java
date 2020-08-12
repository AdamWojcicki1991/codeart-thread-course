package com.codeart.thread.week4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkStealingPoolLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        ExecutorService stealingPool = Executors.newWorkStealingPool();

        AtomicInteger counter = new AtomicInteger();
        stealingPool.execute(new Task(1000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));

        stealingPool.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    static class Task implements Runnable {
        int sleep;
        int id;

        public Task(int sleep, int id) {
            this.sleep = sleep;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "]{" + id + "} Doing work for " + sleep + " ms...");
                Thread.sleep(1000);
                System.out.println("[" + Thread.currentThread().getName() + "]{" + id + "} Done ;)");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
