package cn.element.juc.plan;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 送外卖案例
 */
@Slf4j(topic = "c.SendTakeOut")
public class SendTakeOut {

    private static boolean hasCigarette = false;

    private static boolean hasTakeOut = false;

    //房间锁
    private static final ReentrantLock room = new ReentrantLock();

    //等香烟休息室
    private static final Condition cigaretteWaitSet = room.newCondition();

    //等外卖休息室
    private static final Condition takeOutWaitSet = room.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            room.lock();

            try {
                log.debug("有烟没? [{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没有烟,先歇着!");
                    try {
                        cigaretteWaitSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没? [{}]", hasCigarette);

                if (hasCigarette) {
                    log.debug("可以开始干活了!");
                } else {
                    log.debug("没干成活...");
                }
            } finally {
                room.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            room.lock();

            try {
                log.debug("外卖送到没? [{}]", hasTakeOut);
                while (!hasTakeOut) {
                    log.debug("没有外卖,先歇着!");
                    try {
                        takeOutWaitSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有外卖没? [{}]", hasTakeOut);

                if (hasTakeOut) {
                    log.debug("可以开始干活了!");
                } else {
                    log.debug("没干成活...");
                }
            } finally {
                room.unlock();
            }
        }, "小明").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            room.lock();

            try {
                log.debug("美团外卖温馨提醒,外卖已经送达...");
                hasTakeOut = true;
                takeOutWaitSet.signal();
            } finally {
                room.unlock();
            }
        }, "美团外卖").start();

        new Thread(() -> {
            room.lock();

            try {
                log.debug("烟送来了...");
                hasCigarette = true;
                cigaretteWaitSet.signal();
            } finally {
                room.unlock();
            }
        }, "滴滴送烟").start();
    }
}
