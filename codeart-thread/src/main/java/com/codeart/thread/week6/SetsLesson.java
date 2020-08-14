package com.codeart.thread.week6;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetsLesson {
    public static void main(String[] args) throws InterruptedException {
        print("Started...");

//        Set<Integer> set = new HashSet<>(numbers(0, 1000));
//        Set<Integer> set = Collections.newSetFromMap(new ConcurrentHashMap<>());
//        Set<Integer> set = ConcurrentHashMap.newKeySet();
        Set<Integer> set = new CopyOnWriteArraySet<>();

        set.addAll(numbers(0, 1000));

        AtomicInteger removals = new AtomicInteger();

        Runnable remover = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    if (set.remove(i)) {
                        removals.incrementAndGet();
                    }
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(remover);
        executorService.execute(remover);
        executorService.execute(remover);
        executorService.execute(remover);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        print("Removals: " + removals.get());

        print("DONE");
    }

    static List<Integer> numbers(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }

    static void print(String output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
