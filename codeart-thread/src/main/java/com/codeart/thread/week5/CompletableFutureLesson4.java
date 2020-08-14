package com.codeart.thread.week5;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

public class CompletableFutureLesson4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

//        CompletableFuture<Long> asyncLong = CompletableFuture.supplyAsync(() -> {
//            try {
//                System.out.println("[" + Thread.currentThread().getName() + "] Computing...");
//                Thread.sleep(500);
//                System.out.println("[" + Thread.currentThread().getName() + "] DONE");
//                return 42L;
//            } catch (InterruptedException e) {
//                throw new IllegalStateException(e);
//            }
//        });
//
//        CompletableFuture<String> asyncString = CompletableFuture.supplyAsync(() -> {
//            try {
//                System.out.println("[" + Thread.currentThread().getName() + "] Computing...");
//                Thread.sleep(500);
//                System.out.println("[" + Thread.currentThread().getName() + "] DONE");
//                return "Consumer name is: John Doe";
//            } catch (InterruptedException e) {
//                throw new IllegalStateException(e);
//            }
//        });

        CompletableFuture<Long> futureValue = new MyTask(-5).square();

//        CompletableFuture<Long> future = futureValue.whenComplete(new BiConsumer<Long, Throwable>() {
//            @Override
//            public void accept(Long aLong, Throwable throwable) {
//                if (throwable == null) {
//                    System.out.println("Everything is fine: " + aLong);
//                } else {
//                    System.out.println("Something went wrong: " + throwable.getMessage());
//                }
//            }
//        });

        CompletableFuture<Optional<Long>> handle = futureValue.handle(new BiFunction<Long, Throwable, Optional<Long>>() {
            @Override
            public Optional<Long> apply(Long aLong, Throwable throwable) {
                if (throwable == null) {
                    System.out.println("Everything is fine: " + aLong);
                    return Optional.of(aLong);
                } else {
                    System.out.println("Something went wrong: " + throwable.getMessage());
                    return Optional.empty();
                }
            }
        });

        System.out.println("Result is: " + handle.get());

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    static class MyTask {
        private long value;

        public MyTask(long value) {
            this.value = value;
        }

        public CompletableFuture<Long> square() {
            return CompletableFuture.supplyAsync(() -> {
                if (value <= 0) {
                    throw new IllegalArgumentException("This must be at least 1");
                }
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "] Computing...");
                    Thread.sleep(500);
                    System.out.println("[" + Thread.currentThread().getName() + "] DONE");
                    return 42l;
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            });
        }
    }
}
