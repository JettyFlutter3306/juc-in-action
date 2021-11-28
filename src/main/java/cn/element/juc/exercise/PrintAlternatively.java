package cn.element.juc.exercise;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替打印一串字母
 * 线程1输出 a 5次,线程2输出 b 5次,线程输出 c 5次,
 * 现在要求输出 abcabcabcabcabc
 *
 * 输出内容         等待标记        下一个标记
 *  a               1               2
 *  b               2               3
 *  c               3               1
 */
@Slf4j(topic = "c.PrintAlternatively")
public class PrintAlternatively {

    private static class WaitNotify {
        //等待标记
        private int flag;

        //循环次数
        private final int loopNumber;

        public WaitNotify(int flag, int loopNumber) {
            this.flag = flag;
            this.loopNumber = loopNumber;
        }

        public void print(String str, int waitFlag, int nextFlag) {
            for (int i = 0; i < loopNumber; i++) {
                synchronized (this) {
                    while (flag != waitFlag) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.print(str);
                    flag = nextFlag;
                    this.notifyAll();
                }
            }
        }
    }

    private static class AwaitSignal extends ReentrantLock {
        private final int loopNumber;

        public AwaitSignal(int loopNumber) {
            this.loopNumber = loopNumber;
        }

        /**
         * 交替打印内容
         * @param str           内容
         * @param current       当前线程的休息室
         * @param next          下一间休息室
         */
        public void print(String str, Condition current, Condition next) {
            for (int i = 0; i < loopNumber; i++) {
                lock();

                try {
                    current.await();
                    System.out.print(str);
                    next.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    unlock();
                }
            }
        }
    }

    /**
     * 使用 wait-notify 设计范式
     */
    public void synchronizedPrintByNotify() {
        WaitNotify wn = new WaitNotify(1, 5);

        new Thread(() -> wn.print("a", 1, 2), "t1").start();
        new Thread(() -> wn.print("b", 2, 3), "t2").start();
        new Thread(() -> wn.print("c", 3, 1), "t3").start();
    }

    /**
     * 使用 ReentrantLock 设计范式
     */
    public void synchronizedPrintByReentrantLock() {
        //参数传入5次循环
        AwaitSignal as = new AwaitSignal(5);

        //分别穿件对应的休息室
        Condition a = as.newCondition();
        Condition b = as.newCondition();
        Condition c = as.newCondition();

        new Thread(() -> as.print("a", a, b), "t1").start();
        new Thread(() -> as.print("b", b, c), "t2").start();
        new Thread(() -> as.print("c", c, a), "t3").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        as.lock();
        try {
            //唤醒a线程
            System.out.println("开始......");
            a.signal();
        } finally {
            as.unlock();
        }
    }

    /**
     * 使用LockSupport设计范式
     */
    public void synchronizedPrintByLockSupport() {

    }

    public static void main(String[] args) {
        PrintAlternatively pa = new PrintAlternatively();

//        pa.synchronizedPrintByNotify();
        pa.synchronizedPrintByReentrantLock();
    }


}
