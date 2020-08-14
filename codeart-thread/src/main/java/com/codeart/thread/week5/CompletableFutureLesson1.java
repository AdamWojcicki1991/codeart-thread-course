package com.codeart.thread.week5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureLesson1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        CompletableFuture<Long> completableFuture = new CompletableFuture<>();
        CompletableFuture<String> future = CompletableFuture.completedFuture(" I've already got this one!");
        CompletableFuture<Long> longCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] Doing hard work...");
                Thread.sleep(500);
                completableFuture.complete(42L);
                return 123L;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }/*, Executors.newFixedThreadPool(4)*/);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "] Doing hard work...");
                    Thread.sleep(500);
                    completableFuture.complete(42L);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };


        Long result = longCompletableFuture.get();
//        String result = future.get();

//        Executor executor = Executors.newFixedThreadPool(4);
//        executor.execute(task);

//        Long result = completableFuture.get();

        System.out.println("[" + Thread.currentThread().getName() + "] Result is: " + result);

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }
}
