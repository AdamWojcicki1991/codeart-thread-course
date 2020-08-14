package com.codeart.thread.week5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureLesson6 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        CompletableFuture<String> blocksAsync = new AsyncTask("Blocks").fetch();
        CompletableFuture<String> tapeAsync = new AsyncTask("Tapes").fetch();
        CompletableFuture<String> paperAsync = new AsyncTask("Paper").fetch();

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
                System.out.println("[" + Thread.currentThread().getName() + "] Something went wrong. Unable to proceed with your order");
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] Parcel: [" + String.join(", ", elements) + "]");
            }
        });

        future.get();

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    static class AsyncTask {
        private String result;

        public AsyncTask(String result) {
            this.result = result;
        }

        public CompletableFuture<String> fetch() {
            return CompletableFuture.supplyAsync(() -> {
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
}
