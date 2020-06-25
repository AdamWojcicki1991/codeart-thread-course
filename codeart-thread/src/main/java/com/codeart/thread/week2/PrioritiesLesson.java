package com.codeart.thread.week2;

public class PrioritiesLesson {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " = Starting...");

        Thread t0 = new Thread(new Task("Running in MAX priority"));
        t0.setPriority(Thread.MAX_PRIORITY);
        t0.start();

        Thread t1 = new Thread(new Task("Running in MIN priority"));
        t1.setPriority(Thread.MIN_PRIORITY);
        t1.start();

        System.out.println(Thread.currentThread().getName() + " = Done...");
    }

    static class Task implements Runnable {
        private final String message;

        public Task(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 30; i++) {
                    System.out.println("[" + Thread.currentThread().getName() + "]" + message);
                    Thread.sleep(1_000);
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
