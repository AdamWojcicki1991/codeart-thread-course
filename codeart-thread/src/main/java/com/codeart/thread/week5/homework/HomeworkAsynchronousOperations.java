package com.codeart.thread.week5.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HomeworkAsynchronousOperations {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        CompletableFuture<String> blocksAsync = new AsyncTask(new AssortmentProcessor().processItem()).fetch();
        CompletableFuture<String> tapeAsync = new AsyncTask(new AssortmentProcessor().processItem()).fetch();
        CompletableFuture<String> paperAsync = new AsyncTask(new AssortmentProcessor().processItem()).fetch();

        CompletableFuture<List<String>> future = blocksAsync.thenCombine(tapeAsync, (block, tape) -> {
            List<String> elements = new ArrayList<>();
            elements.add(block);
            elements.add(tape);
            return elements;
        }).thenCombine(paperAsync, (elements, paper) -> {
            elements.add(paper);
            return elements;
        }).whenComplete((elements, error) -> {
            if (error != null) {
                System.out.println("[" + Thread.currentThread().getName() + "] Something went wrong. Unable to proceed with your order!");
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] Parcel: [" + String.join(", ", elements) + "]");
            }
        });

        CompletableFuture<Optional<List<String>>> handle = future.handle((items, throwable) -> {
            if (throwable == null) {
                System.out.println("[" + Thread.currentThread().getName() + "] Parcel is ready to go with items!: " + items);
                return Optional.of(items);
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] System can not prepare correct parcel!: " + throwable.getMessage());
                return Optional.empty();
            }
        });

        handle.get();

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    private final static class AsyncTask {
        private final String result;

        public AsyncTask(final String result) {
            this.result = result;
        }

        public CompletableFuture<String> fetch() {
            return CompletableFuture.supplyAsync(() -> {
                if (!(result.equals("Blocks") || result.equals("Tapes") || result.equals("Paper"))) {
                    throw new IllegalArgumentException("Item: " + result + " is not in order package!");
                }
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "] Preparing result: " + result + "...");
                    Thread.sleep(500);
                    System.out.println("[" + Thread.currentThread().getName() + "] DONE [" + result + "]");
                    return result;
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            });
        }
    }

    private final static class AssortmentProcessor {
        private static final Random RANDOM = new Random();

        private String processItem() {
            switch (RANDOM.nextInt(8)) {
                case 0:
                    return "Blocks";
                case 1:
                    return "Tapes";
                case 2:
                    return "Paper";
                case 3:
                    return "Rubber";
                case 4:
                    return "Scissors";
                case 5:
                    return "Glue";
                case 6:
                    return "Pencils";
                case 7:
                    return "Pens";
                default:
                    return "";
            }
        }
    }
}
