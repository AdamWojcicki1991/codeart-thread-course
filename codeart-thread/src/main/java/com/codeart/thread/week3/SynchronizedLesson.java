package com.codeart.thread.week3;

public class SynchronizedLesson {
    private static class Counter {
        private long value = 0;
        private long otherValue = 0;
        private long noSynchronizedValue = 0;
        private static long instances = 0;

        public static synchronized void incrementInstances() {
            instances = instances + 1;
        }

        public void increment() {
            noSynchronizedValue = noSynchronizedValue + 1;
            synchronized (this) {
                decrement();
                value = value + 1;
            }
        }

        public synchronized void decrement() {
            otherValue = otherValue - 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        Counter counter = new Counter();
        Counter otherCounter = new Counter();

        Runnable incrementTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    counter.incrementInstances();
                    otherCounter.incrementInstances();
                }
            }
        };

        Thread t0 = new Thread(incrementTask);
        Thread t1 = new Thread(incrementTask);
        Thread t2 = new Thread(incrementTask);

        t0.start();
        t1.start();
        t2.start();

        t0.join();
        t1.join();
        t2.join();

        System.out.println("Counter value is: " + counter.value + ", expected: 3000");
        System.out.println("Counter otherValue is: " + counter.otherValue + ", expected: -3000");
        System.out.println("Counter instances is: " + counter.instances);
        System.out.println("Counter noSynchronizedValue is: " + counter.noSynchronizedValue);
        System.out.println("Done...");
    }
}
