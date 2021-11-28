package cn.element.juc.normal;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 同步代码块设计范式
 */
@Slf4j(topic = "c.NormalForm")
public class SynchronizedBlockForm {

    //用来同步的锁
    private static final Object lock = new Object();

    //t2运行标记,代表t2是否执行过
    private static boolean t2Run = false;

    public static void main(String[] args) {
        //wait notify版实现,让线程2先打印,然后线程1再打印
        Thread t1 = new Thread(() -> {
           synchronized (lock) {
               //如果t2没有执行过
               while (!t2Run) {
                   try {
                       //t1先等一会
                       lock.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }

               log.debug("1");
           }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("2");
                t2Run = true;
                lock.notify();
            }
        }, "t2");

        //方式二: 使用LockSupport类的park()和unpark()方法
//        Thread t1 = new Thread(() -> {
//            LockSupport.park();
//            log.debug("1");
//        }, "t1");
//
//        Thread t2 = new Thread(() -> {
//            log.debug("2");
//            LockSupport.unpark(t1);
//        }, "t2");

        t1.start();
        t2.start();
    }

}
