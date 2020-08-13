package com.codeart.thread.week5;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class FutureLesson2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("[" + Thread.currentThread().getName() + "] Starting...");

        Callable<Long> longRunningTask = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                System.out.println("[" + Thread.currentThread().getName() + "] Starting hard work...");
                Thread.sleep(15000);
                System.out.println("[" + Thread.currentThread().getName() + "] Done, returning result...");
                return 123L;
            }
        };

        MyTask myTask1 = new MyTask(10);
        MyTask myTask2 = new MyTask(11);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<Long> future = executorService.submit(longRunningTask);

        Future<Long> future1 = executorService.submit(myTask1);
        Future<Long> future2 = executorService.submit(myTask2);

        List<Future<Long>> result = executorService.invokeAll(Arrays.asList(myTask1, myTask2));

        for (Future<Long> longFuture : result) {
            System.out.println("[" + Thread.currentThread().getName() + "] Result is: " + longFuture.get());
        }

//        System.out.println("[" + Thread.currentThread().getName() + "] Result is: " + result);

//        Long value1 = future1.get();
//        System.out.println("Value1= " + value1);
//
//        Long value2 = future2.get();
//        System.out.println("Value2= " + value2);

//        if (!future.isDone()) {
//            Thread.sleep(1000);
//        }
//
//        if (!future.isDone()) {
//            boolean cancel = future.cancel(true);
//            System.out.println("is canceled: " + cancel);
//            Long result = future.get();
//            System.out.println("Result: " + result);
//        }

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    static class MyTask implements Callable<Long> {
        long value;

        public MyTask(long value) {
            this.value = value;
        }

        @Override
        public Long call() throws Exception {
            if (value <= 0) {
                throw new IllegalArgumentException("Value can not be lower than 1");
            }
            System.out.println("[" + Thread.currentThread().getName() + "] Starting hard work...");
            Thread.sleep(500);
            System.out.println("[" + Thread.currentThread().getName() + "] Done, returning result...");
            return value * value;
        }
    }
}
