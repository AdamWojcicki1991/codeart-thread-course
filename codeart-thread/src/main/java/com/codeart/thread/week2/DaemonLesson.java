package com.codeart.thread.week2;

import java.text.NumberFormat;

public class DaemonLesson {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        Runnable diagnosticTask = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Runtime runtime = Runtime.getRuntime();
                        NumberFormat format = NumberFormat.getInstance();

                        StringBuilder stringBuilder = new StringBuilder();
                        long maxMemory = runtime.maxMemory();
                        long allocatedMemory = runtime.totalMemory();
                        long freeMemory = runtime.freeMemory();

                        stringBuilder.append("free memory: " + format.format(freeMemory / 1024) + "\n");
                        stringBuilder.append("allocated memory: " + format.format(allocatedMemory / 1024) + "\n");
                        stringBuilder.append("max memory: " + format.format(maxMemory / 1024) + "\n");
                        stringBuilder.append("total free memory: " + format.format((freeMemory + maxMemory - allocatedMemory) / 1024) + "\n");
                        System.out.println(stringBuilder.toString());
                        Thread.sleep(1_000);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
        Thread diagnosticThread = new Thread(diagnosticTask);
        diagnosticThread.setDaemon(true);
        diagnosticThread.start();

        System.out.println("Is Daemon: " + diagnosticThread.isDaemon());

        Thread.sleep(5_000);

        System.out.println("Done...");
    }
}
