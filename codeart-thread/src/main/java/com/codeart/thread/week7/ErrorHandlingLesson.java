package com.codeart.thread.week7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ErrorHandlingLesson {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingLesson.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");

        Runnable longTask = () -> {
            try {
                logger.info("Starting long task...");
                Thread.sleep(1000);
                logger.info("DONE");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1),
                (r, executor) -> logger.error("Task cannot be executed on thread poll: {}", executor));

        threadPoolExecutor.execute(longTask); //running
        threadPoolExecutor.execute(longTask); //queued
        threadPoolExecutor.execute(longTask); //rejected

        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);

        logger.info("DONE...");
    }
}
