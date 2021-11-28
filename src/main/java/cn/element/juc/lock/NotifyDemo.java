package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;

/**
 * obj.wait() 让进入object监视器的线程得到waitSet等待
 * obj.notify() 在object上正在waitSet等待的线程中挑一个唤醒
 * obj.notifyAll() 让object上正在waitSet等待的线程全部唤醒
 * 它们都是线程之间进行协作的手段,都是属于Object类的方法,必须获得次对象的锁,才能调用这几个方法
 * 他们的状态为TIMED_WAITING
 */
@Slf4j(topic = "c.NotifyDemo")
public class NotifyDemo {

    final static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("执行...");
                try {
                    lock.wait(1000);  //让占有lock的线程一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其他代码...");
            }
        }, "t1").start();

//        new Thread(() -> {
//            synchronized (lock) {
//                log.debug("执行....");
//                try {
//                    lock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log.debug("其他代码...");
//            }
//        }, "t2").start();

        Thread.sleep(500);

        log.debug("唤醒lock上的其他线程");

        synchronized (lock) {
            //唤醒对象上的一个线程
//            lock.notify();

            //唤醒所有的等待线程
            lock.notifyAll();
        }
    }
}
