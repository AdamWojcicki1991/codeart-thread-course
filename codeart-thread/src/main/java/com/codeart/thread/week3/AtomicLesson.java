package com.codeart.thread.week3;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");
        Counter counter = new Counter();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    counter.increment();
                    counter.unsafeIncrement();
                }
            }
        };

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t0.start();
        t1.start();
        t2.start();

        t0.join();
        t1.join();
        t2.join();

        System.out.println("Counter value is: " + counter.value.get());
        System.out.println("Counter unsafe-value is: " + counter.unsafeValue);

        System.out.println("DONE");
    }

    static class Counter {
        AtomicLong value = new AtomicLong(0);
        long unsafeValue = 0;

        void increment() {
            value.incrementAndGet();
        }

        void unsafeIncrement() {
            unsafeValue++;
        }
    }
}
