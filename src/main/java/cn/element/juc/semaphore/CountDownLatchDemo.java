package cn.element.juc.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.element.juc.util.ThreadUtil.sleep;

/**
 * CountDownLatch用来进行线程间的同步协作
 * 等待所有线程完成倒计时
 * 其中构造参数用来出初始化等待计数值
 * await()方法用来等待计数值归零
 * countDown()用来让计数减1
 */
@Slf4j(topic = "c.CountDownLatchDemo")
public class CountDownLatchDemo {

    public static void testCountDownLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            log.debug("begin....");
            sleep(1);
            latch.countDown();
            log.debug("end....");
        }).start();

        new Thread(() -> {
            log.debug("begin....");
            sleep(2);
            latch.countDown();
            log.debug("end....");
        }).start();

        new Thread(() -> {
            log.debug("begin....");
            sleep(1.5);
            latch.countDown();
            log.debug("end....");
        }).start();

        log.debug("waiting...");
        latch.await();
        log.debug("waiting end...");
    }

    public static void testCountDownPool() {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        pool.submit(() -> {
            log.debug("begin....");
            sleep(1);
            latch.countDown();
            log.debug("end.... {}", latch.getCount());
        });

        pool.submit(() -> {
            log.debug("begin....");
            sleep(1.5);
            latch.countDown();
            log.debug("end.... {}", latch.getCount());
        });

        pool.submit(() -> {
            log.debug("begin....");
            sleep(2);
            latch.countDown();
            log.debug("end.... {}", latch.getCount());
        });

        pool.submit(() -> {
            try {
                log.debug("waiting...");
                latch.await();
                log.debug("waiting end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 使用CountDownLatch来模拟游戏玩家加载
     */
    public static void testGameLoading() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        String[] loadingBar = new String[10];
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = i;  //记录当前的索引,lambda表达式不能引用变化的局部变量

            pool.submit(() -> {
                for (int j = 0; j <= 100; j++) {
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    loadingBar[index] = j + "%";
                    System.out.print("\r" + Arrays.toString(loadingBar));
                }

                latch.countDown();
            });
        }

        latch.await();
        System.out.println("\n游戏开始...");
        pool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
//        testCountDownLatch();
//        testCountDownPool();
        testGameLoading();



    }
}