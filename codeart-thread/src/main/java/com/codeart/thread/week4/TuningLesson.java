package com.codeart.thread.week4;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TuningLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("[" + Thread.currentThread().getName() + "] I am Full, sorry bro");
                    }
                });

        ThreadPoolExecutor cachedExecutor = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("[" + Thread.currentThread().getName() + "] I am Full, sorry bro");
                    }
                });

        Executor daemonExecutor = Executors.newFixedThreadPool(4, new ThreadFactory() {
            AtomicInteger id = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "my-daemon-thread-" + id.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            }
        });

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "] Doing hard work...");
                    Thread.sleep(200);
                    System.out.println("[" + Thread.currentThread().getName() + "] DONE");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);

        daemonExecutor.execute(task);
        daemonExecutor.execute(task);

        Thread.sleep(1000);

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }
}
