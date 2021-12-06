package cn.element.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.IBlockingQueue")
class IBlockingQueue<T> {

    //1.任务队列
    private final Deque<T> queue = new ArrayDeque<>();

    //2.锁
    private final ReentrantLock lock = new ReentrantLock();

    //3.生产者条件变量
    private final Condition fullWaitSet = lock.newCondition();

    //4.消费者条件变量
    private final Condition emptyWaitSet = lock.newCondition();

    //5.容量
    private final int capacity;

    public IBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 带超时的阻塞获取
     */
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();

        try {
            //将timeout统一转换为纳秒
            long l = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    //返回的是剩余的时间
                    if (l <= 0) {
                        return null;
                    }

                    l = emptyWaitSet.awaitNanos(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            T t = queue.removeFirst();
            fullWaitSet.signal();  //唤醒生产者线程

            return t;
        } finally {
            lock.unlock();
        }
    }

    //阻塞获取
    public T take() {
        lock.lock();

        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            T t = queue.removeFirst();
            fullWaitSet.signal();  //唤醒生产者线程

            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞添加
     */
    public void put(T task) {
        lock.lock();

        try {
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列 {}...", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取大小
     */
    public int size() {
        lock.lock();

        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时时间的阻塞添加
     */
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();

        try {
            long l = timeUnit.toNanos(timeout);

            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列 {}...", task);

                    if (l <= 0) {
                        return false;
                    }

                    l = fullWaitSet.awaitNanos(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();

            return true;
        } finally {
            lock.unlock();
        }
    }


    public void tryPut(RejectStrategy<T> rejectStrategy, T task) {
        lock.lock();

        try {
            //判断队列是否已满
            if (queue.size() == capacity) {
                rejectStrategy.reject(this, task);
            } else {  //有空闲
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
