package cn.element.juc.workerthread;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步模式之工作线程
 *
 * 让有限多的工作线程(Worker Thread)来轮流异步处理无限多的任务
 * 也可以将其归类为分工模式,它的典型应用就是线程池,也体现了经典的设计模式中的享元模式
 *
 * 创建多大容量的线程池才合适?
 * 1.过小会导致程序不能充分地利用系统资源,容易导致饥饿
 * 2.过大会导致更多的线程上下文切换,占用更多的内存
 *
 * strategy:
 *      1) CPU密集型运算
 *      通常采用CPU 核数 + 1能够实现最优的CPU利用率,+1是保证当前线程由于页缺失故障或者其他原因导致暂停
 *      额外的这个线程就能顶上去,保证CPU的时钟周期不被浪费
 *
 *      2) I/O密集型运算
 *      CPU不总是处于繁忙状态,例如,当执行业务计算时,这时候会使用CPU资源,当执行IO操作时,远程RPC调用时,包括
 *      进行数据库操作时,这时候CPU就闲下来了,就可以利用多线程提高它的利用率
 *
 *      经验公式:
 *          线程数 = 核数 * 期望 CPU 利用率 * 总时间(CPU计算时间 + 等待时间) / CPU计算时间
 *          e.g:
 *              4核CPU计算时间是50%,其他等待时间是50%,期望CPU被100%利用,根据公式
 *              4 * 100% * 100% / 50% = 8
 *
 */
@Slf4j(topic = "c.AsynchronousThreadDemo")
public class AsynchronousThreadDemo {

    /**
     * 测试使用Timer工具类
     */
    public static void testTimer() {
        Timer timer = new Timer();

        /*
         * 因为任务是串行的,那么只要有一个任务调度失败,其他的任务也会失败
         * 推荐使用ScheduledThreadPool类进行定时调度
         */
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task2");
            }
        };

        log.debug("start...");
        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);
    }

    /**
     * 测试使用ScheduledThreadPool定时线程池
     * 此为Timer类的替代方案
     */
    public static void testScheduledThreadPool() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        pool.schedule(() -> {
            log.debug("task1...");
//            int i = 1 / 0;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);

        pool.schedule(() -> log.debug("task2..."), 1, TimeUnit.SECONDS);
    }

    public static void testInterval() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        log.debug("start...");

//        pool.scheduleAtFixedRate(() -> {
//            log.debug("running...");
//        }, 1, 1, TimeUnit.SECONDS);

        pool.scheduleWithFixedDelay(() -> {
            log.debug("running...");
        }, 1, 1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
//        testTimer();
//        testScheduledThreadPool();
        testInterval();



    }


}
