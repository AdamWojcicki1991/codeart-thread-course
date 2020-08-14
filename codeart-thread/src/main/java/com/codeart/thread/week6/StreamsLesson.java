package com.codeart.thread.week6;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class StreamsLesson {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        print("Starting...");

        Stream<Integer> numbers = Stream.of(4, 12, 42, 0, 9, 87, 11, 34, 57, 17);

        Instant now = Instant.now();

//        numbers.map(StreamsLesson::doubleIt).forEach(StreamsLesson::print);
//        Using only parallel() method is dangerous because it uses all thread pools in application and we don't have
//        any control of it
//        numbers.parallel().map(StreamsLesson::doubleIt).forEach(StreamsLesson::print);

        ForkJoinPool pool = new ForkJoinPool(2);

        pool.submit(() -> numbers
                .parallel()
                .map(StreamsLesson::doubleIt)
//                .sequential() -> changing stream from parallel to sequential (one thread running task) we can not mix parallel streams and sequential stream because of lazy instantiating
                .forEach(StreamsLesson::print)).get();

        Instant end = Instant.now();

        Duration duration = Duration.between(now, end);
        print("Took:" + duration.toMillis() + " ms ");

        print("DONE");
    }

    static Integer doubleIt(int value) {
        try {
            print("Doubling value: " + value);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        return value * 2;
    }

    static void print(Object output) {
        System.out.println(
                String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output)
        );
    }
}
