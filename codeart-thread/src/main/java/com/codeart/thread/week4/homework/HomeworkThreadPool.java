package com.codeart.thread.week4.homework;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class HomeworkThreadPool {
    public static void main(String[] args) {
        System.out.println("[" + Thread.currentThread().getName() + "] START");

        final Counter counter = new Counter();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, 4,
                30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                (task, executor) -> {
                    if (counter.increment() > 9) executor.shutdown();
                    System.out.println(diagnosticMonitor(executor, counter.value));
                });

        Runnable task = () -> {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] Task in progress...");
                Thread.sleep(100);
                System.out.println("[" + Thread.currentThread().getName() + "] Task DONE!");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        };

        IntStream.range(0, 24).forEach(i -> threadPoolExecutor.execute(task));

        shutdown(counter, threadPoolExecutor);

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    private final static class Counter {
        private final AtomicLong value = new AtomicLong(0);

        private long increment() {
            return value.incrementAndGet();
        }
    }

    private static String diagnosticMonitor(final ThreadPoolExecutor executor, final AtomicLong lostTaskCount) {
        return "[" + Thread.currentThread().getName() +
                "] Queue capacity is full - [" +
                "Task count: " + executor.getTaskCount() +
                " | Pool size: " + executor.getPoolSize() +
                " | Keep Alive Time [sec]: " + executor.getKeepAliveTime(TimeUnit.SECONDS) +
                " | Queue size: " + executor.getQueue().size() +
                " | Task lost count: " + lostTaskCount + "]";
    }

    private static void shutdown(Counter counter, ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolExecutor.isShutdown()) {
            System.out.println("RejectedExecution shutdown - Task lost count was to big: " + counter.value);
        } else {
            System.out.println("Application shutdown - Task lost count was under 10: " + counter.value);
            threadPoolExecutor.shutdown();
        }
    }
}
