package cn.element.juc.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.element.juc.util.ThreadUtil.sleep;

/**
 * CyclicBarrier循环栅栏的使用
 */
@Slf4j(topic = "c.CyclicBarrierDemo")
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CyclicBarrier barrier = new CyclicBarrier(2, () -> log.debug("task1 task2 finished..."));

        pool.submit(() -> {
            log.debug("task1 begins...");
            sleep(1);

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        pool.submit(() -> {
            log.debug("task2 begins...");
            sleep(2);

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();
    }
}