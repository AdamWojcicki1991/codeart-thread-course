package com.codeart.thread.week5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CompletableFutureLesson5 {
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

        CompletableFuture<Long> asyncLong2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] Computing...");
                Thread.sleep(1500);
                System.out.println("[" + Thread.currentThread().getName() + "] DONE");
                return 42L;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        Executor executor = Executors.newSingleThreadExecutor();

        CompletableFuture<Void> future = asyncLong.thenAcceptAsync(value -> {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] Accepting value...");
                Thread.sleep(2000);
                System.out.println("[" + Thread.currentThread().getName() + "] Consumed value is: " + value);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }, executor);

        asyncLong.get();

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");

        CompletableFuture<Long> completableFuture = new CompletableFuture<>();
        completableFuture.complete(42L);
        completableFuture.completeExceptionally(new RuntimeException("Opps..."));

        System.out.println("Result is " + completableFuture.get());
    }
}
