package com.codeart.thread.week6.homework;

import java.lang.ref.SoftReference;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class HomeworkCache {
    public static void main(String[] args) throws InterruptedException {
        print("Starting...");
        new ProcessRunner().run(5);
        print("DONE");
    }

    private static final class ProcessRunner {
        private final RunnableProcessor runnableProcessor;

        public ProcessRunner() {
            this.runnableProcessor = new RunnableProcessor();
        }

        private void run(final int threadsCount) throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
            executorService.execute(runnableProcessor.cleanValue());
            executorService.execute(runnableProcessor.putValue());
            executorService.execute(runnableProcessor.putNextValue());
            executorService.execute(runnableProcessor.putNextValue());

            Thread.sleep(30_000);

            executorService.shutdownNow();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private static final class RunnableProcessor {
        private final Cache cache;

        public RunnableProcessor() {
            this.cache = new Cache();
        }

        private Runnable cleanValue() {
            return () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    cache.getKeySet().forEach(key -> {
                        cache.tryEvict(key, 10);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
            };
        }

        private Runnable putValue() {
            return () -> {
                IntStream.range(0, 50).forEach(number -> {
                    Integer key = (new Random()).nextInt(10);
                    String value = UUID.randomUUID().toString();
                    cache.put(key, value);
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        System.err.println("[putValue] Got interrupted exception. Breaking the loop");
                    }
                });
            };
        }

        private Runnable putNextValue() {
            return () -> {
                IntStream.range(0, 50).forEach(number -> {
                    Integer key = 2;//(new Random()).nextInt(12);
                    CacheElement elem = cache.get(key);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.err.println("[putNextValue] Got interrupted exception. Breaking the loop");
                    }
                });
            };
        }
    }

    private static final class Cache {
        private final ConcurrentHashMap<Integer, SoftReference<CacheElement>> cache = new ConcurrentHashMap<>();

        public synchronized void put(final Integer key, final String value) {
            if (key != null) {
                SoftReference<CacheElement> cacheElement = new SoftReference<>(new CacheElement(key, value));
                cache.put(key, cacheElement);
                print("Element added: key=" + key + " value=" + value + " update time=" + cacheElement.get().getLastUpdateTime());
            }
        }

        public synchronized CacheElement get(final Integer key) {
            SoftReference<CacheElement> softReference = cache.get(key);
            CacheElement cacheElement = null;
            if (softReference != null) {
                cacheElement = softReference.get();
                cacheElement.setLastUpdateTime(LocalDateTime.now());
                print("Element taken: key=" + cacheElement.getKey() + " value= " + cacheElement.getValue() + " last update time=" + cacheElement.getLastUpdateTime());
            }
            return cacheElement;
        }

        public CacheElement remove(final String key) {
            return cache.remove(key).get();
        }

        public synchronized void tryEvict(final Integer key, final long notUsedTime) {
            CacheElement cacheElement = cache.get(key).get();
            boolean wasNotUsed = !Duration.between(cacheElement.getLastUpdateTime(), LocalDateTime.now()).minusSeconds(notUsedTime).isNegative();
            if (wasNotUsed) {
                cache.remove(key);
                print("Element evicted: key=" + key);
            }
        }

        public int size() {
            return cache.size();
        }

        public KeySetView<Integer, SoftReference<CacheElement>> getKeySet() {
            return cache.keySet();
        }
    }

    private static final class CacheElement {
        private Integer key;
        private String value;
        private LocalDateTime lastUpdateTime;

        public CacheElement(final Integer key, final String value) {
            this.key = key;
            this.value = value;
            this.lastUpdateTime = LocalDateTime.now();
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(final Integer key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public LocalDateTime getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(final LocalDateTime updateTime) {
            this.lastUpdateTime = updateTime;
        }
    }

    private static void print(final String output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
