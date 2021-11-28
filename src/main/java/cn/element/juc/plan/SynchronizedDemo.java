package cn.element.juc.plan;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用synchronized修饰方法
 * 等价于:
 *      class T {
 *          public void test01() {
 *              synchronized(this) {
 *
 *              }
 *          }
 *      }
 */
@Slf4j(topic = "c.Synchronized")
public class SynchronizedDemo {

    private static int counter = 0;

    public synchronized void increment() {
        counter++;
    }

    public synchronized void decrement() {
        counter--;
    }

    public synchronized int getCounter() {
        return counter;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo a = new SynchronizedDemo();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                a.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                a.decrement();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("value: {}", a.getCounter());
    }
}
