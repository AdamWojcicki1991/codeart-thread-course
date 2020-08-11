package com.codeart.thread.week3;

import java.util.Random;

public class ScopeLesson {
    public static void main(String[] args) {
        System.out.println("Starting...");

        Counter counter = new Counter();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int randomValue = random.nextInt(20);
                System.out.println("Increment Value for: " + randomValue + " is: " + counter.increment(randomValue));
            }
        };

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        Thread t4 = new Thread(task);

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        System.out.println("DONE");
    }

    static class Counter {
        //Not correct working with Threads without
        long value = 0;

        //Correct working in Threads because of method variable scope
        int increment(int maxValue) {
            int localValue;
            localValue = maxValue * 3;
            return localValue;
        }
    }
}
