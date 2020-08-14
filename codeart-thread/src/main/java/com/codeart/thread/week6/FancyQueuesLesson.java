package com.codeart.thread.week6;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FancyQueuesLesson {
    public static void main(String[] args) throws InterruptedException {
        print("Starting...");

//        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
//        SynchronousQueue is putting value to queue by writer queue.put() and blocking on this method
//        and wait for method that is reading from this queue queue.take()
//        TransferQueue<Integer> queue = new LinkedTransferQueue<>();
//        TransferQueue is similar to SynchronousQueue and but have additional methods like queue.transfer() and this
//        method will be blocking one evan we have queue.put() (put method will normally run) method earlier in code
        Exchanger<Integer> exchanger = new Exchanger<>();
//        Exchanger is running multiple threads but block thread on exchanger.exchange() method and then allows next
//        thread to exchange a value. Process of exchanging continue till the end of task

        Runnable writer = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                try {
                    for (int i = 0; i < 10; i++) {
                        int value = random.nextInt(100);
                        print("Putting value to exchanger: " + value);
                        int newValue = exchanger.exchange(value);
                        print("Received value to exchanger: " + newValue);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

//        Runnable writer = new Runnable() {
//            @Override
//            public void run() {
//                Random random = new Random();
//                try {
//                    for (int i = 0; i < 10; i++) {
//                        print("Putting  value to queue");
//                        queue.put(random.nextInt(100));
//                        print("OK");
//                        Thread.sleep(500);
//                    }
//                    print("Putting last value");
//                    queue.transfer(999);
//                    print("FINAL");
//                } catch (InterruptedException e) {
//                    throw new IllegalStateException(e);
//                }
//            }
//        };

//        Runnable reader = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    for (int i = 0; i < 15; i++) {
//                        print("Reading value from queue");
//                        Integer take = queue.take();
//                        print("Taken value is: " + take);
//                    }
//                } catch (InterruptedException e) {
//                    throw new IllegalStateException(e);
//                }
//            }
//        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(writer);
        executorService.execute(writer);
//        executorService.execute(reader);
//        executorService.execute(reader);

        print("DONE");
    }

    static void print(String output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
