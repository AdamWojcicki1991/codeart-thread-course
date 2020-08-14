package com.codeart.thread.week6;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MapsLesson {
    public static void main(String[] args) throws InterruptedException {
        print("Starting...");
//        Optimal way to use map in Threads safe for save operations for read operation we can have mismatch
        Map<String, Integer> map = new ConcurrentHashMap<>();
//        Not optimal because of returning SynchronizedMap class witch uses keyword synchronized in every method so we can't make parallel operations on threads
//        Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());
        map.put("test", 0);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    int size = map.size();
                    map.put(String.valueOf(size), size);
                    map.computeIfPresent("test", (key, value) -> value + 1);
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        print("Result is: " + map.size());
        print("Result is: " + map.get("test"));

        print("DONE");
    }

    static void print(String output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
