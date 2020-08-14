package com.codeart.thread.week5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CompletableFutureLesson3 {
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
                Thread.sleep(500);
                System.out.println("[" + Thread.currentThread().getName() + "] DONE");
                return 97L;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

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

        CompletableFuture<String> asyncTask = asyncLong.thenCompose(new Function<Long, CompletionStage<String>>() {
            @Override
            public CompletionStage<String> apply(Long value) {
                return asyncAnswer(value);
            }
        });

        CompletableFuture<Void> future = asyncLong.acceptEither(asyncLong2, value -> {
            System.out.println("The faaster one was: " + value);
        });

        CompletableFuture<Void> future1 = asyncLong.thenAcceptBoth(asyncLong2, new BiConsumer<Long, Long>() {
            @Override
            public void accept(Long value1, Long value2) {
                System.out.println("There are: " + value1 + " and: " + value2);
            }
        });

        CompletableFuture<Object> future2 = CompletableFuture.anyOf(asyncLong, asyncLong2, asyncString);

        CompletableFuture<Void> future3 = CompletableFuture.allOf(asyncLong, asyncLong2, asyncString);

        System.out.println("[" + Thread.currentThread().getName() + "] Async Result is: " + future3.get());

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    static CompletableFuture<String> asyncAnswer(Long value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] Computing...");
                Thread.sleep(500);
                System.out.println("[" + Thread.currentThread().getName() + "] DONE");
                return "Answer to all question is: " + value;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
