package cn.element.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ThreadPoolDemo")
public class ThreadPoolDemo {

    public static void main(String[] args) {
        IThreadPool pool = new IThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            //1) 死等
//            queue.put(task);

            //2) 带超时等待
//            queue.offer(task, 500, TimeUnit.MILLISECONDS);

            //3) 放弃任务的执行
//            log.debug("放弃 {}", task);

            //4) 抛出异常
//            throw new RuntimeException("任务执行失败" + task);

            //5) 让调用者自己去执行任务
            task.run();

        });

        for (int i = 0; i < 6; i++) {
            int j = i;
            pool.execute(() -> log.debug("{}", j));
        }
    }
}

