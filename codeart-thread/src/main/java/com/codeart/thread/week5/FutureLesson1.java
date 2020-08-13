package com.codeart.thread.week5;

import java.util.concurrent.*;

public class FutureLesson1 {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        Callable<Long> longRunningTask = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                System.out.println("[" + Thread.currentThread().getName() + "] Starting hard work...");
                Thread.sleep(1500);
                System.out.println("[" + Thread.currentThread().getName() + "] Done, returning result...");
                return 123L;
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<Long> future1 = executorService.submit(longRunningTask);
        Future<Long> future2 = executorService.submit(longRunningTask);

        System.out.println("[" + Thread.currentThread().getName() + "] Doing, something other...");

        while (!future1.isDone() && !future2.isDone()) {
            System.out.println("future1: " + future1.isDone() + ", future2: " + future2.isDone());
            Thread.sleep(300);
        }

        System.out.println("Future1.value = " + future1.get() + " | Future2.value = " + future2.get());

//        System.out.println("Future1.value = " + future1.get(500, TimeUnit.MILLISECONDS) + " | Future2.value = " + future2.get(500, TimeUnit.MILLISECONDS));

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }
}
