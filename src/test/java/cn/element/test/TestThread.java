package cn.element.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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

    @Test
    public void test04() {

        
    }

}
