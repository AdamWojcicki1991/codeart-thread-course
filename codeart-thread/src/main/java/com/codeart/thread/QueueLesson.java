package com.codeart.thread;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.*;

public class QueueLesson {
    public static void main(String[] args) throws InterruptedException {
        print("Starting...");

// ArrayBlockingQueue thread is constantly blocking on method queue.take() and waiting for queue.put() method to put some element on queue
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);

        Runnable producer = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(500);
                        int number = random.nextInt(100);
                        print("Adding number: " + number);
                        queue.put(number);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        int number = queue.take();
                        print("Read number: " + number);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.execute(producer);
        executorService.execute(producer);
        executorService.execute(consumer);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        print("DONE");
    }

    static void print(String output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
