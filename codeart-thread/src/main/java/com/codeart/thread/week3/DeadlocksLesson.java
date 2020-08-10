package com.codeart.thread.week3;

public class DeadlocksLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Task().doTheJob();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        });

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new OtherTask().doTheJob();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        });
        t0.start();
        t1.start();

        t0.join();
        t1.join();

        System.out.println("DONE");
    }

    static Object lock1 = new Object();
    static Object lock2 = new Object();

    static class Task {
        public void doTheJob() throws InterruptedException {
            System.out.println("[" + Thread.currentThread().getName() + "] Task trying to acquire lock1");
            synchronized (lock1) {
                System.out.println("[" + Thread.currentThread().getName() + "] Holding lock1");
                Thread.sleep(1000);

                synchronized (lock2) {
                    System.out.println("[" + Thread.currentThread().getName() + "] Holding lock2");
                    Thread.sleep(1000);
                    System.out.println("[" + Thread.currentThread().getName() + "] Releasing lock2");
                }
                System.out.println("[" + Thread.currentThread().getName() + "] Releasing lock1");
            }
        }
    }

    static class OtherTask {
        public void doTheJob() throws InterruptedException {
            System.out.println("[" + Thread.currentThread().getName() + "] OtherTask trying to acquire lock1");
            synchronized (lock1) {
                System.out.println("[" + Thread.currentThread().getName() + "] Holding lock1");
                Thread.sleep(1000);

                synchronized (lock2) {
                    System.out.println("[" + Thread.currentThread().getName() + "] Holding lock2");
                    Thread.sleep(1000);
                    System.out.println("[" + Thread.currentThread().getName() + "] Releasing lock2");
                }
                System.out.println("[" + Thread.currentThread().getName() + "] Releasing lock1");
            }
        }
    }

// Deadlock situation
//    static class OtherTask {
//        public void doTheJob() throws InterruptedException {
//            System.out.println("[" + Thread.currentThread().getName() + "] OtherTask trying to acquire lock2");
//            synchronized (lock2) {
//                System.out.println("[" + Thread.currentThread().getName() + "] Holding lock2");
//                Thread.sleep(1000);
//
//                synchronized (lock1) {
//                    System.out.println("[" + Thread.currentThread().getName() + "] Holding lock1");
//                    Thread.sleep(1000);
//                    System.out.println("[" + Thread.currentThread().getName() + "] Releasing lock1");
//                }
//                System.out.println("[" + Thread.currentThread().getName() + "] Releasing lock1");
//            }
//        }
//    }
}
