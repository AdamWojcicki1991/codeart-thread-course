package com.codeart.thread.week7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class PerformanceLesson {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceLesson.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");

        Map<Integer, String> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();

        int loops = 1000;
        int threads = 100;
        int repeats = 10;

        logger.info("Starting warmup...");
        //Warmup for Java Virtual Machine to optimize code.
        for (int i = 0; i < repeats; i++) {
            benchmark(synchronizedMap, loops, threads);
            benchmark(concurrentMap, loops, threads);
        }

        logger.info("Starting benchmark...");
        //Benchmark
        LongAdder synchronizedAdder = new LongAdder();
        LongAdder concurrentAdder = new LongAdder();
        for (int i = 0; i < repeats; i++) {
            synchronizedAdder.add(benchmark(synchronizedMap, loops, threads).toMillis());
            concurrentAdder.add(benchmark(concurrentMap, loops, threads).toMillis());
        }

        logger.info("==================== RESULT =====================");
        logger.info("synchronizedMap: {} ms, concurrentMap: {} ms", synchronizedAdder.doubleValue() / repeats, concurrentAdder.doubleValue() / repeats);

        logger.info("DONE...");
    }

    static Duration benchmark(Map<Integer, String> map, int loops, int threads) throws InterruptedException {
        Instant start = Instant.now();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executorService.execute(new PerformanceTask(map, loops));
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
        Instant end = Instant.now();
        return Duration.between(start, end);
    }

    static class PerformanceTask implements Runnable {
        private final Map<Integer, String> map;
        private final int loops;

        public PerformanceTask(Map<Integer, String> map, int loops) {
            this.map = map;
            this.loops = loops;
        }

        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < loops; i++) {
                map.compute(random.nextInt(), (key, value) -> key.toString());
            }
        }
    }
}
