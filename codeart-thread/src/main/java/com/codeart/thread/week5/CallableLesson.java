package com.codeart.thread.week5;

import java.util.concurrent.*;

public class CallableLesson {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        Callable<Long> task = new Callable() {
            @Override
            public Long call() throws Exception {
                System.out.println("[" + Thread.currentThread().getName() + "] Doing hard work...");
                Thread.sleep(500);
                return 123L;
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(4);

        Future<Long> feature = executor.submit(task);

        Long result = feature.get();

        System.out.println("Computation result is: " + result);

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }
}
