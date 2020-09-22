package com.codeart.thread.week7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class CorrectnessMultiThreadLesson {
    private static final Logger logger = LoggerFactory.getLogger(CorrectnessMultiThreadLesson.class);

    public static void main(String[] args) throws InterruptedException {
        Store store = new Store();

        Set<Integer> ids = new HashSet<>();

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        // Method latch.await() is blocking all Threads on it and then run latch.countDown() and run all threads in one time.
        CountDownLatch latch = new CountDownLatch(1);

        Runnable task = () -> {
            try {
                latch.await();
                store.add("product");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        };

        int items = 100;
        for (int i = 0; i < items; i++) {
            executorService.execute(task);
        }

        latch.countDown();
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        logger.info("Store items {}, expected {}", store.size(), items);
    }

    static class Store {
        private final Map<Integer, String> inventory = new ConcurrentHashMap<>();

        // Because in this method we dont use Atomic class we need to use keyword synchronized to have safe thread method.
        public /*synchronized*/ Integer add(String name) {
            int id = inventory.size();
            inventory.put(id, name);
            return id;
        }

        public String getById(Integer id) {
            return inventory.get(id);
        }

        public Integer size() {
            return inventory.size();
        }
    }
}
