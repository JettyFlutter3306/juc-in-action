package cn.element.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.LockSupport;

import static cn.element.juc.util.ThreadUtil.sleep;

//52讲
@Slf4j(topic = "c.Test1")
public class TestThread {

    @Test
    public void test01() {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                log.debug("running......");
            }
        };

        t1.start();

        log.debug("running");
    }

    @Test
    public void test02() {
        Thread t = new Thread(() -> {
            log.debug("running...");
        }, "t1");

        t.start();

        log.debug("running....");
    }

    @Test
    public void test03() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            log.debug("running....");

            Thread.sleep(2000);

            return 100;
        });

        Thread t = new Thread(futureTask, "t1");

        log.debug("running....");

        t.start();  //开启线程

        Integer value = futureTask.get();  //阻塞等待状态

        log.debug("{}", value);
    }

    /**
     * 测试yield()方法
     */
    @Test
    public void tesYield() {
        Runnable task1 = () -> {
            int count = 0;
            while (true) {
                System.out.println("-------->1  " + count++);
            }
        };

        Runnable task2 = () -> {
            int count = 0;
            while (true) {
                Thread.yield();
                System.out.println("---------------->2  " + count++);
            }
        };

        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");

        t1.start();
        t2.start();
    }

    static class TwoPhaseTermination {
        private Thread monitor;

        public void start() {
            monitor = new Thread(() -> {
                while (true) {
                    Thread current = Thread.currentThread();

                    if (current.isInterrupted()) {
                        log.debug("料理后事");
                        break;
                    }

                    try {
                        Thread.sleep(1000);
                        log.debug("执行监控记录");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //重新设置打断标记
                        current.interrupt();
                    }
                }
            });

            monitor.start();
        }

        public void end() {
            monitor.interrupt();
        }
    }

    @Test
    public void testPhaseTermination() {
        TwoPhaseTermination tp = new TwoPhaseTermination();
        tp.start();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tp.end();
    }

    /**
     * 测试LockSupport的park()方法
     */
    @Test
    public void testPark() {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unPark...");
            log.debug("打断状态: {}", Thread.currentThread().isInterrupted());
        }, "t1");

        t1.start();

        sleep(1);

        t1.interrupt();
    }

    static int counter = 0;
    static final Object lock = new Object();
    /**
     * 测试synchronized
     */
    @Test
    public void testSynchronized() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                for (int i = 0; i < 5000; i++) {
                    counter++;
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                for (int i = 0; i < 5000; i++) {
                    counter--;
                }
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(counter);
    }
}
