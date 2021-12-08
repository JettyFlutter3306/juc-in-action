package cn.element.juc.aqs;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.AqsDemo")
public class AqsDemo {

    public static void main(String[] args) {
        ILock lock = new ILock();

        new Thread(() -> {
            lock.lock();

            try {
                Thread.sleep(1000);
                log.debug("locking...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();

            try {
                log.debug("locking...");
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        }, "t2").start();
    }
}