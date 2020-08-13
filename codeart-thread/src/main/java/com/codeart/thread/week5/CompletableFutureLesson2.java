package com.codeart.thread.week5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

public class CompletableFutureLesson2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        CompletableFuture<Long> asyncLong = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] Computing...");
                Thread.sleep(500);
                System.out.println("[" + Thread.currentThread().getName() + "] DONE");
                return 42L;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

//        Long result = asyncLong.get();
//
//        System.out.println("[" + Thread.currentThread().getName() + "] Value is: " + result);

        CompletableFuture<String> asyncString = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] Computing...");
                Thread.sleep(500);
                System.out.println("[" + Thread.currentThread().getName() + "] DONE");
                return "Consumer name is: John Doe";
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> asyncResult = asyncLong
                .thenApply(value -> {
                    return value * value;
                })
                .thenApply(value -> {
                    try {
                        Thread.sleep(500);
                        return value / 4;
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .thenApply(value -> {
                    return "Final value is: " + value;
                });

        CompletableFuture<String> asyncTask = asyncLong.thenCombine(asyncString, new BiFunction<Long, String, String>() {
            @Override
            public String apply(Long number, String text) {
                return "Combined value is: " + text + " - " + number;
            }
        });

//        System.out.println("[" + Thread.currentThread().getName() + "] Async Result is: " + asyncResult.get());
        System.out.println("[" + Thread.currentThread().getName() + "] Async Result is: " + asyncTask.get());

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }
}
