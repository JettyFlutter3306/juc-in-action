package cn.element.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * JDK内置的线程池工具类的使用
 *
 * public ThreadPoolExecutor(int corePoolSize,
 *                           int maximumPoolSize,
 *                           long keepAliveTime,
 *                           TimeUnit unit,
 *                           BlockingQueue<Runnable> workQueue,
 *                           ThreadFactory threadFactory,
 *                           RejectedExecutionHandler handler)
 *
 * corePoolSize     核心线程数目(最多保留的线程数)
 * maximumPoolSize  最大线程数目
 * keepAliveTime    生存时间-针对救急线程
 * unit             时间单位-针对救急线程
 * workQueue        阻塞队列
 * threadFactory    线程工厂-可以为线程创建时起名字
 * handler          拒绝策略
 */
@Slf4j(topic = "c.ThreadPoolExecutorDemo")
public class ThreadPoolExecutorDemo {

    /**
     * 测试线程工厂类
     */
    public static void testThreadFactory() {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private final AtomicInteger t = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "my-pool-" + t.getAndIncrement());
            }
        });

        pool.execute(() -> log.debug("1"));
        pool.execute(() -> log.debug("2"));
        pool.execute(() -> log.debug("3"));
    }

    /**
     * 测试submit()方法
     */
    public static void testSubmit() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        //使用lambda表达式重写Callable接口的call()方法
        Future<String> future = pool.submit(() -> {
            log.debug("running...");
            Thread.sleep(1000);
            return "ok";
        });

        log.debug("{}", future.get());
    }

    /**
     * 测试invokeAll()方法
     */
    public static void testInvokeAll() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        List<Callable<String>> list = new ArrayList<>();

        Collections.addAll(list,
                () -> {
                    log.debug("begin");
                    Thread.sleep(1000);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(500);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(2000);
                    return "3";
                }
        );

        List<Future<String>> futures = pool.invokeAll(list);

        futures.forEach(future -> {
            try {
                log.debug("{}", future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 测试invokeAny()方法
     */
    public static void testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        List<Callable<String>> list = new ArrayList<>();

        Collections.addAll(list,
                () -> {
                    log.debug("begin 1");
                    Thread.sleep(1000);
                    log.debug("end 1");
                    return "1";
                },
                () -> {
                    log.debug("begin 2");
                    Thread.sleep(500);
                    log.debug("end 2");
                    return "2";
                },
                () -> {
                    log.debug("begin 3");
                    Thread.sleep(2000);
                    log.debug("end 3");
                    return "3";
                }
        );

        String s = pool.invokeAny(list);

        log.debug("{}", s);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        testThreadFactory();
//        testSubmit();
//        testInvokeAll();
        testInvokeAny();


    }
}
