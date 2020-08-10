package com.codeart.thread.week3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaphoreLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        Restaurant restaurant = new Restaurant(5);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Person person = new Person();
                    boolean success = restaurant.stepInto(person);
                    if (success) {
                        System.out.println("[" + Thread.currentThread().getName() + "] I enjoy this place...");
                        Thread.sleep(1000);
                        restaurant.leave(person);
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] no more seats :(");
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        Thread t0 = new Thread(task, "PERSON_1");
        Thread t1 = new Thread(task, "PERSON_2");
        Thread t2 = new Thread(task, "PERSON_3");
        Thread t3 = new Thread(task, "PERSON_4");
        Thread t4 = new Thread(task, "PERSON_5");
        Thread t5 = new Thread(task, "PERSON_6");
        Thread t6 = new Thread(task, "PERSON_7");
        Thread t7 = new Thread(task, "PERSON_8");
        Thread t8 = new Thread(task, "PERSON_9");
        Thread t9 = new Thread(task, "PERSON_10");

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();

        t0.join();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
        t8.join();
        t9.join();

        System.out.println("DONE");
    }

    static class Person {

    }

    static class Restaurant {
        int seats;
        List<Person> persons = new ArrayList<>();
        Semaphore semaphore;

        public Restaurant(int seats) {
            semaphore = new Semaphore(seats);
            this.seats = seats;
        }

        public boolean stepInto(Person person) {
            System.out.println("[" + Thread.currentThread().getName() + "] stepping into restaurant...");
            boolean acquire = semaphore.tryAcquire();
            if (acquire) {
                System.out.println("[" + Thread.currentThread().getName() + "] Eating...");
                persons.add(person);
            } else {
                System.out.println("There are already " + persons.size() + " people in here");
            }
            return acquire;
        }

        public void leave(Person person) {
            if (persons.remove(person)) {
                semaphore.release();
            }
        }
    }
}
