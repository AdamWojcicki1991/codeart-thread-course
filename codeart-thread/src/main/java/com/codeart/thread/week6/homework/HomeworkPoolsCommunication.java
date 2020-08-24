package com.codeart.thread.week6.homework;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class HomeworkPoolsCommunication {
    public static void main(String[] args) throws InterruptedException {
        print("Starting...");
        new ThreadCommunicationExecutor(2, 1).executeCommunication();
        print("DONE");
    }

    private static final class ThreadCommunicationExecutor {
        private final RunnableProcessor runnableProcessor;
        private final int producersCount;
        private final int consumersCount;

        public ThreadCommunicationExecutor(final int producersCount, final int consumersCount) {
            this.runnableProcessor = new RunnableProcessor();
            this.producersCount = producersCount;
            this.consumersCount = consumersCount;
        }

        private void executeCommunication() throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(producersCount + consumersCount);
            IntStream.range(0, producersCount).forEach(producer -> {
                executorService.execute(runnableProcessor.getProducer());
            });
            IntStream.range(0, consumersCount).forEach(producer -> {
                executorService.execute(runnableProcessor.getConsumer());
            });
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private static final class RunnableProcessor {
        private final BlockingQueue<String> queue;

        public RunnableProcessor() {
            this.queue = new ArrayBlockingQueue<>(1);
        }

        private Runnable getProducer() {
            return () -> {
                IntStream.range(0, 100).forEach(number -> {
                    try {
                        Thread.sleep(500);
                        print("Write Book: " + number);
                        queue.put("Book: " + number);
                    } catch (InterruptedException e) {
                        System.err.println("[Producer] Got Interrupted, breaking the loop!");
                        Thread.currentThread().interrupt();
                    }
                });
            };
        }

        private Runnable getConsumer() {
            return () -> {
                IntStream.range(0, 100).forEach(number -> {
                    try {
                        String book = queue.take();
                        print("Read: " + book);
                    } catch (Exception e) {
                        System.err.println("[Consumer] Got Interrupted, breaking the loop!");
                        Thread.currentThread().interrupt();
                    }
                });
            };
        }
    }

    private static void print(final String output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
