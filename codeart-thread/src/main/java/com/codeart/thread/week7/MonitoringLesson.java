package com.codeart.thread.week7;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MonitoringLesson {
    private static final Logger logger = LoggerFactory.getLogger(MonitoringLesson.class);

    public static void main(String[] args) {
        logger.info("Starting...");

        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);

        MetricRegistry registry = new MetricRegistry();
        registry.register("executor.queue.length", (Gauge<Integer>) workQueue::size);

        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 2,
                10, TimeUnit.SECONDS,
                workQueue
        );

        Runnable longTask = () -> {
            try {
                logger.info("Doing hard work...");
                Thread.sleep(5000);
                logger.info("DONE");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        };

        for (int i = 0; i < 100; i++) {
            executor.execute(longTask);
        }

        logger.info("DONE");
    }
}
