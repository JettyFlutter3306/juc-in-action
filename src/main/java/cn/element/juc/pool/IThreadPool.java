package cn.element.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.IThreadPool")
class IThreadPool {

    //任务队列
    private final IBlockingQueue<Runnable> tasksQueue;

    //线程集合
    private final Set<Worker> workers = new HashSet<>();

    //核心的线程数
    private final int coreSize;

    //获取任务的超时时间
    private final long timeout;

    private final TimeUnit unit;

    private final RejectStrategy<Runnable> rejectStrategy;

    public IThreadPool(int coreSize, long timeout, TimeUnit unit, int capacity, RejectStrategy<Runnable> rejectStrategy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.unit = unit;
        this.tasksQueue = new IBlockingQueue<>(capacity);
        this.rejectStrategy = rejectStrategy;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        //当任务处理没有包括coreSize时,直接交给Worker对象执行
        //如果任务处理超过coreSize时,加入任务队列
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker {}", worker);

                workers.add(worker);
                worker.start();
            } else {
                log.debug("加入任务队列 {}", task);
                tasksQueue.tryPut(rejectStrategy, task);
            }
        }
    }

    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //执行任务
            //1) 当task不为空,执行任务
            //2) 当task执行完毕,再接着从任务队列获取任务并执行
//            while (task != null || (task = tasksQueue.take()) != null) {
            while (task != null || (task = tasksQueue.poll(timeout, unit)) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }

            synchronized (workers) {
                log.debug("worker被移除{}", this);
                workers.remove(this);
            }
        }
    }
}
