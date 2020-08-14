package com.codeart.thread.week6;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListsLesson {
    public static void main(String[] args) throws InterruptedException {
        print("Starting...");

        List<Integer> numbers = new CopyOnWriteArrayList<>(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
//        List<Integer> numbers = Collections.synchronizedList(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
//        List<Integer> numbers = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));

        print(numbers);

        Runnable reader = new Runnable() {
            @Override
            public void run() {
                try {
                    for (Integer number : numbers) {
                        print("Read item from list:" + number);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        Runnable writer = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                try {
                    for (int i = 0; i < 10; i++) {
                        numbers.add(i, random.nextInt(20));
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(reader);
        executorService.execute(writer);
        executorService.execute(reader);
        executorService.execute(writer);
        executorService.execute(reader);
        executorService.execute(writer);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        print(numbers);

        print("DONE");
    }

    static void print(Object output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
