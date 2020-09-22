package com.codeart.thread.week8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Random;

public class ThreadLocalLesson {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalLesson.class);

    public static void main(String[] args) throws InterruptedException {
        Computation computation = new Computation();
        Thread t1 = new Thread(computation);
        Thread t2 = new Thread(computation);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        logger.info("DONE");
    }

    static class Computation implements Runnable {
        private static final ThreadLocal<Integer> localNumber = new ThreadLocal<>();
        private final Random random = new Random();

        @Override
        public void run() {
            final int value = random.nextInt(1000);
            MDC.put("number", value + "");
            logger.info("Setting local number as: {}", value);
            localNumber.set(value);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            logger.info("Local number is: {}", localNumber.get());
            MDC.remove("number");
        }
    }
}
