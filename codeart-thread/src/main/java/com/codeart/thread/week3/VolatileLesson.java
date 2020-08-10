package com.codeart.thread.week3;

import java.util.concurrent.atomic.AtomicLong;

public class VolatileLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        Counter counter = new Counter();
        Thread t0 = new Thread(counter);

//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100000; i++) {
//                    counter.increment();
//                }
//            }
//        };
//
//        Thread t0 = new Thread(task);
//        Thread t1 = new Thread(task);
//        Thread t2 = new Thread(task);
//
        t0.start();
//        t1.start();
//        t2.start();
//
//        t0.join();
//        t1.join();
//        t2.join();

        Thread.sleep(1500);

        counter.stopPlease();

//        System.out.println("Current value is: " + counter.value);

        System.out.println("DONE");
    }

    static class Counter implements Runnable {
        AtomicLong value = new AtomicLong();
        volatile boolean isRunning = true;

        public long increment() {
            return value.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    long current = increment();
                    System.out.println("Incremented counter to: " + current);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        public void stopPlease() {
            System.out.println("Setting isRunning to false");
            isRunning = false;
        }
    }

//    static class Counter {
//        volatile long value = 0;
//
//        public long increment() {
//            return value++;
//        }
//    }
}
