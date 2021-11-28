package cn.element.juc.plan;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Room")
public class Room {

    private static int counter = 0;

    public void increment() {
        synchronized (this) {
            counter++;
        }
    }

    public void decrement() {
        synchronized (this) {
            counter--;
        }
    }

    public int getCounter() {
        synchronized (this) {
            return counter;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t1");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("value: {}", room.getCounter());
    }

}
