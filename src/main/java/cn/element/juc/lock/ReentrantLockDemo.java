package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁: 可重入是指同一个线程如果首次获得了这把锁,那么因为它是这把锁的拥有者
 * 因此有权利再次获取这把锁,如果是不可重入锁,那么第二次获得锁时,自己也会被锁住
 *
 * 相对于synchronized关键字有以下特点:
 *      1.可中断
 *      2.可以设置超时时间
 *      3.可以设置公平锁
 *      4.支持多个条件变量
 * 和synchronized一样,都支持可重入
 */
@Slf4j(topic = "c.ReentrantLock")
public class ReentrantLockDemo {

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public static void m1() {
        reentrantLock.lock();

        try {
            log.debug("enter m1");
            m2();
        } finally {  //finally块中执行释放锁
            reentrantLock.unlock();
        }
    }

    public static void m2() {
        reentrantLock.lock();

        try {
            log.debug("enter m2");
        } finally {  //finally块中执行释放锁
            reentrantLock.unlock();
        }
    }

    public static void main(String[] args) {
        //加锁
//        reentrantLock.lock();
//
//        try {
//            log.debug("enter main");
//            m1();
//        } finally {  //finally块中执行释放锁
//            reentrantLock.unlock();
//        }

        //测试ReentrantLock可中断
//        Thread t1 = new Thread(() -> {
//            try {
//                //如果没有竞争那么此方法就会获取lock的对象锁
//                //如果有竞争那么就进入阻塞队列,可以被其他线程使用interrupt()方法打断
//                log.debug("尝试获取锁...");
//                reentrantLock.lockInterruptibly();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                log.debug("没有获得锁...");
//
//                return;
//            }
//
//            try {
//                log.debug("获取到锁...");
//            } finally {
//                reentrantLock.unlock();
//            }
//        }, "t1");
//
//        //给主线程加锁,使得线程进入阻塞状态
//        reentrantLock.lock();
//        t1.start();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        log.debug("打断t1");
//        t1.interrupt();

        Thread t1 = new Thread(() -> {
            log.debug("尝试获得锁...");

            try {
                if (!reentrantLock.tryLock(2, TimeUnit.SECONDS)) {
                    log.debug("获取不到锁....");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("获取不到锁...");
                return;
            }

            try {
                log.debug("获得到锁...");
            } finally {
                reentrantLock.unlock();
            }
        }, "t1");

        reentrantLock.lock();
        log.debug("获取到锁...");
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reentrantLock.unlock();
        log.debug("释放了锁...");


    }

}
