package com.codeart.thread.week2;

public class RunnableLesson {
    public static void main(String[] args) {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");
        MyCarTask task = new MyCarTask();
        Thread t0 = new Thread(task);
        t0.start();
        Thread t1 = new Thread(task);
        t1.start();
        Thread t2 = new Thread(task);
        t2.start();

        Thread t3 = new MyDrivingCarThread();
        t3.start();
        Thread t4 = new MyDrivingCarThread();
        t4.start();
        Thread t5 = new MyDrivingCarThread();
        t5.start();

        System.out.println("[" + Thread.currentThread().getName() + "] Done...");
    }

    static abstract class Car {
        public String drive() {
            return "Brum..brum...";
        }
    }

    static class MyCarTask extends Car implements Runnable {
        int counter = 0;

        @Override
        public void run() {
            counter++;
            System.out.println("[" + Thread.currentThread().getName() + "] " + super.drive() + " - rally: " + counter);
        }
    }

    static class MyDrivingCarThread extends Thread {
        int counter = 0;

        @Override
        public void run() {
            counter++;
            System.out.println("[" + Thread.currentThread().getName() + "] I am running in a MyDrivingThread" + " - rally: " + counter);
        }
    }
}
