package com.codeart.thread;

public class ThreadLesson {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " - Hello, World!");

        Thread thread = new Thread("WorkingThread") {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " - I am running in a thread");
            }
        };
        thread.start();

        System.out.println(Thread.currentThread().getName() + " - Done");
    }
}
