package com.codeart.thread.week2;

import java.util.Random;
import java.util.stream.IntStream;

public class HomeworkThreads {
    private final static Random RANDOM = new Random();

    private enum WriterType {
        FIRST_WRITER,
        SECOND_WRITER
    }

    private static class Data {
        private Integer number;

        private synchronized void writeNumber(Integer value) throws InterruptedException {
            while (number != null) {
                wait();
            }
            number = value;
            notify();
        }

        private synchronized Integer readNumber() throws InterruptedException {
            while (number == null) {
                wait();
            }
            Integer tmpNumber = number;
            number = null;
            notify();
            return tmpNumber;
        }
    }

    private static class writeNumberTask implements Runnable {
        private final Data data;
        private final WriterType writerType;
        private Integer number;

        public writeNumberTask(Data data, WriterType writerType) {
            this.data = data;
            this.writerType = writerType;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    number = (writerType == WriterType.FIRST_WRITER) ? RANDOM.nextInt(50) + 1 : RANDOM.nextInt(401) + 100;
                    data.writeNumber(number);
                    System.out.println(Thread.currentThread().getName() + " Generated number from thread: " + number);
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    System.err.println("Kill Thread: " + Thread.currentThread().getName());
                    break;
                }
            }
        }
    }

    private static class readNumberTask implements Runnable {
        private final Data data;
        private Integer sum;

        public readNumberTask(Data data) {
            this.data = data;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Integer number = data.readNumber();
                    sum = IntStream.range(0, number + 1).sum();
                    System.out.println(Thread.currentThread().getName() + " Sum of current number: " + number + " is: " + sum);
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    System.err.println("Kill Thread: " + Thread.currentThread().getName());
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start...");
        Data data = new Data();

        Thread t1 = new Thread(new writeNumberTask(data, WriterType.FIRST_WRITER), "[WRITER_1]");
        Thread t2 = new Thread(new writeNumberTask(data, WriterType.SECOND_WRITER), "[WRITER_2]");
        Thread t3 = new Thread(new readNumberTask(data), "[READER]");
        Thread t4 = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(Thread.currentThread().getName() + " Current number is: " + data.number + " | " +
                            t1.getName() + " -> " + t1.getState() + " | " + t2.getName() + " -> " +
                            t2.getState() + " | " + t3.getName() + " -> " + t3.getState() + " | ");
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    System.err.println("Kill Thread: " + Thread.currentThread().getName());
                    break;
                }
            }
        }, "[MONITOR]");
        t4.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();

        Thread.sleep(30_000);
        System.out.println("Done...");

        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
    }
}
