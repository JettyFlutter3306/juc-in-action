package cn.element.juc.forkjoin;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;

/**
 * 任务拆分和递归之类的分治算法十分类似
 */
@Slf4j(topic = "c.ForkJoinDemo")
public class ForkJoinDemo {

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);

        System.out.println(pool.invoke(new Task(5)));

    }
}