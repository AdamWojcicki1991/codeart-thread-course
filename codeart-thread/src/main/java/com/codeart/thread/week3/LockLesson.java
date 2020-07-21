package com.codeart.thread.week3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");
//        Counter counter = new Counter();
//
//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10000; i++) {
//                    counter.increment();
//                }
//            }
//        };
//
//        Thread t0 = new Thread(task);
//        Thread t1 = new Thread(task);
//        Thread t2 = new Thread(task);
//
//        t0.start();
//        t1.start();
//        t2.start();
//
//        t0.join();
//        t1.join();
//        t2.join();
//
//        System.out.println("Counter value is: " + counter.value);

        Inventory inventory = new Inventory();

//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    for (int i = 0; i < 10; i++) {
//                        inventory.put("pomidor", i);
//                    }
//                } catch (InterruptedException e) {
//                    throw new IllegalStateException(e);
//                }
//            }
//        };

        Runnable writeTask = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        inventory.put("pomidor", i);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        Runnable readTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Integer quantity = inventory.howMany("pomidor");
                    System.out.println("There is: " + quantity + " of pomidors");
                }
            }
        };

//        Thread t0 = new Thread(task);
//        Thread t1 = new Thread(task);

        Thread t0 = new Thread(writeTask);
        Thread t1 = new Thread(readTask);
        Thread t2 = new Thread(readTask);
        Thread t3 = new Thread(readTask);

        t0.start();
        t1.start();
        t2.start();
        t3.start();

        t0.join();
        t1.join();
        t2.join();
        t3.join();

        System.out.println("There is: " + inventory.howMany("pomidor") + " of pomidor in inventory");

        System.out.println("Done...");
    }

//    private static class Inventory {
//        Map<String, Integer> state = new HashMap<>();
//        Lock lock = new ReentrantLock();
//
//        public void put(String item, Integer quantity) throws InterruptedException {
//            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
//                try {
//                    System.out.println("[" + Thread.currentThread().getName() + "] Get lock...");
//                    Thread.sleep(100);
//                    System.out.println("[" + Thread.currentThread().getName() + "] There is: " + howMany("pomidor") + " of pomidor in inventory");
//                    state.put(item, quantity);
//                } finally {
//                    lock.unlock();
//                }
//            } else {
//                System.out.println("[" + Thread.currentThread().getName() + "] Unable to get lock...");
//            }
//        }

    private static class Inventory {
        Map<String, Integer> state = new HashMap<>();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public void put(String item, Integer quantity) throws InterruptedException {
            final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
            if (writeLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                //We modify data state by writeLock!
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "] Get lock...");
                    Thread.sleep(100);
                    System.out.println("[" + Thread.currentThread().getName() + "] There is: " + howMany("pomidor") + " of pomidor in inventory");
                    state.put(item, quantity);
                } finally {
                    lock.writeLock();
                }
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] Unable to get lock...");
            }
        }

        public Integer howMany(String item) {
            final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
            if (readLock.tryLock()) {
                //Read date! We can not modify data by readLock!
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "] READ_LOCK obtained...");
                    Thread.sleep(100);
                    final Integer quantity = state.get(item);
                    return quantity;
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                } finally {
                    System.out.println("[" + Thread.currentThread().getName() + "] READ_LOCK released...");
                    readLock.unlock();
                }
            } else {
                return -1;
            }
        }
    }

    private static class Counter {
        long value = 0;
        Lock lock = new ReentrantLock();

        public void increment() {
            try {
                lock.lock();
                lock.tryLock();
                value = value + 1;
                if (value == 1000) {
                    throw new IllegalStateException("Ups...");
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
