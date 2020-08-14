package com.codeart.thread.week4.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class HomeworkForkJoinPool {
    public static void main(String[] args) {
        System.out.println("[" + Thread.currentThread().getName() + "] START");

        ForkJoinPool joinPool = ForkJoinPool.commonPool();

        String stringInputValue = "123456";

        Integer sum = joinPool.invoke(new CountingTask(stringInputValue));

        System.out.println("Result is: " + sum);

        System.out.println("[" + Thread.currentThread().getName() + "] DONE");
    }

    private final static class CountingTask extends RecursiveTask<Integer> {
        private final String initialString;

        private CountingTask(final String initialString) {
            this.initialString = extractNumber(initialString);
        }

        @Override
        protected Integer compute() {
            System.out.println("[" + Thread.currentThread().getName() + "] Computing value: " + initialString);
            if (initialString.isEmpty() || initialString.length() == 1) {
                return Integer.parseInt(initialString);
            } else {
                List<CountingTask> tasks = getCountingTasks(initialString);
                tasks.forEach(ForkJoinTask::fork);
                return tasks.stream().mapToInt(ForkJoinTask::join).sum();
            }
        }
    }

    private static String extractNumber(final String str) {
        if (str == null || str.isEmpty()) {
            return "0";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char character : str.toCharArray()) {
            if (Character.isDigit(character)) {
                stringBuilder.append(character);
            }
        }
        return stringBuilder.toString();
    }

    private static List<CountingTask> getCountingTasks(final String initialString) {
        List<CountingTask> tasks = new ArrayList<>();
        tasks.add(new CountingTask((initialString.substring(0, initialString.length() / 2))));
        tasks.add(new CountingTask((initialString.substring(initialString.length() / 2))));
        return tasks;
    }
}
