package com.codeart.thread.week7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ExceptionsLesson {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionsLesson.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");
        Runnable task = () -> {
            logger.info("Doing hard work...");
            throw new RuntimeException("Oops...");
        };
        // Global ExceptionHandler for whole thread when we have some exception that we dont predict to have in app.
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                logger.error("Got uncaught exception from thread {}, error: {}", t.getName(), e.getMessage()/*,e*/));

        Thread t0 = new Thread(task);
        t0.start();

        // ExceptionHandler dedicated for one thread when we use library class we can set this handler outside a class.
        t0.setUncaughtExceptionHandler((t, e) -> logger.error("CUSTOM EXCEPTION HANDLER:", e));

        // ExceptionHandler dedicated for one thread when we use library class we can set this handler outside a class
        // used inside executorService.
        ExecutorService executorService = Executors.newFixedThreadPool(4, r -> {
            Thread t1 = new Thread(task);
            t1.setUncaughtExceptionHandler((t, e) -> logger.error("CUSTOM EXCEPTION HANDLER:", e));
            return t1;
        });
        // Callable example.
        Callable<Long> compute = () -> {
            logger.info("Computing big number...");
            Thread.sleep(500);
            throw new RuntimeException("Can't compute a value");
        };

        ExecutorService service = Executors.newFixedThreadPool(4);
        Future<Long> submit = service.submit(compute);

        logger.info("Going too sleep...");
        Thread.sleep(2000);
        logger.info("OK!");

        // ExceptionHandler dedicated for one executorService using Callable we need to use method submit.get() to invoke it.
        try {
            submit.get();
        } catch (ExecutionException e) {
            logger.error("Exception from future: {}", e.getMessage(), e);
        }

        logger.info("DONE...");
    }
}
