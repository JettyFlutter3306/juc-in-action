package cn.element.juc.workerthread;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 任务调度线程池的应用之定时任务
 */
public class TimingTaskDemo {

    /**
     * 实现在每周四 20:00:00 执行定时任务
     */
    public static void testTimingTask() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        LocalDateTime timeNow = LocalDateTime.now();  //获取当前时间
        LocalDateTime timeFuture = timeNow.withHour(20)  //获取未来时间
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .with(DayOfWeek.THURSDAY);

        //如果当前时间 > 本周时间,那么久必须找到下周时间
        if (timeNow.compareTo(timeFuture) > 0) {
            timeFuture = timeFuture.plusWeeks(1);
        }

        long initialDelay = Duration.between(timeNow, timeFuture).toMillis();  //时间差
        long interval = 1000 * 60 * 60 * 24 * 7;  //一周间隔

        System.out.println(initialDelay);
        System.out.println(interval);

        pool.scheduleAtFixedRate(() -> System.out.println("每周四定时任务开启...."), initialDelay, interval, TimeUnit.MICROSECONDS);
    }

    public static void main(String[] args) {
        testTimingTask();

    }
}
