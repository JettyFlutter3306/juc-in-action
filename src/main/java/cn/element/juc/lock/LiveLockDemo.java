package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;

/**
 * 活锁: 活锁出现在两个线程互相改变对方的结束条件,最后谁也无法结束
 */
@Slf4j(topic = "c.LiveLock")
public class LiveLockDemo {

    private static int count = 10;

    private final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            while (count > 0) {  //期望减到0退出循环
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                count--;

                log.debug("count: {}", count);
            }
        }, "t1").start();

        new Thread(() -> {
            while (count < 20) {  //期望减到0退出循环
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                count++;

                log.debug("count: {}", count);
            }
        }, "t1").start();
    }
}
