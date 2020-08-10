package com.codeart.thread.week3;

public class LivelockLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        Object spoon = new Object();
        Person wife = new Person(spoon);
        Person husband = new Person(spoon);

        Thread t0 = new Thread(new Runnable() {
            @Override
            public void run() {
                wife.eatWith(husband);
            }
        }, "WIFE");

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                husband.eatWith(wife);
            }
        }, "HUSBAND");

        t0.start();
        t1.start();

        t0.join();
        t1.join();

        System.out.println("DONE");
    }

    static class Person {
        final Object spoon;
        boolean isHungry = true;

        public Person(Object spoon) {
            this.spoon = spoon;
        }

// Livelock situation
//        public void eatWith(Person person) {
//            while (isHungry) {
//                synchronized (spoon) {
//                    if (person.isHungry) {
//                        System.out.println("[" + Thread.currentThread().getName() + "]" + " Please, you first");
//                    } else {
//                        System.out.println("[" + Thread.currentThread().getName() + "]" + " Eating...");
//                        isHungry = false;
//                    }
//                }
//            }
//        }

        public void eatWith(Person person) {
            while (isHungry) {
                synchronized (spoon) {
                    System.out.println("[" + Thread.currentThread().getName() + "]" + " Eating ...");
                    isHungry = false;
                }
            }
        }
    }
}
