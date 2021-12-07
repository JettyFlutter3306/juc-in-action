package cn.element.juc.forkjoin;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * 1 ~ n的整数和
 */
@Slf4j(topic = "c.Task")
public class Task extends RecursiveTask<Integer> {

    private final int n;

    public Task(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n == 1) {
            return 1;
        }

        Task task = new Task(n - 1);
        task.fork();  //拆分,让一个线程去执行次任务
        log.debug("fork() {} + {}", n, task);

        //获取任务的结果然后合并结果得到最终结果
        Integer ans = n + task.join();
        log.debug("join() {} + {} = {}", n, task, ans);

        return ans;
    }

    @Override
    public String toString() {
        return "{" + n + "}";
    }
}
