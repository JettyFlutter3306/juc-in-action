package cn.element.juc.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * Semaphore
 * 信号量,用来限制同时访问共享资源的线程上限
 */
@Slf4j(topic = "c.SemaphoreDemo")
public class SemaphoreDemo {

    public static void main(String[] args) {
        //1.创建一个Semaphore对象,参数是许可数量
        Semaphore semaphore = new Semaphore(3);

        //2.10个线程同时运行,运行之前首先要申请许可,原理还是AQS,队列同步器
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    log.debug("running....");
                    Thread.sleep(1000);
                    log.debug("end....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}