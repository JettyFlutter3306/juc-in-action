package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * wait,notify,notifyAll必须配合Object Monitor一起使用,而park,unPark不必
 * park & unpark是以线程为单位来[阻塞]和[唤醒]线程,而notify只能随机唤醒一个等待线程
 * notifyAll是唤醒所有等待的线程,就不那么精确
 * park & unpark 可以先unpark,而wait & notify不能先notify
 *
 * 每个线程都有自己的一个Parker对象,由三部分组成_counter,_cond和_mutex
 */
@Slf4j(topic = "c.LockSupport")
public class LockSupportDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("park...");

            LockSupport.park();

            log.debug("resume...");
        }, "t1");

        t1.start();

        Thread.sleep(2000);

        log.debug("unPark...");

        LockSupport.unpark(t1);
    }

}
