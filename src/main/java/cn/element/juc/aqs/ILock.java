package cn.element.juc.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义不可重入锁
 */
public class ILock implements Lock {

    private final ISynchronizer synchronizer = new ISynchronizer();

    /**
     * 声明自定义的同步器类
     */
    static class ISynchronizer extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                //加上锁了
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);

            return true;
        }

        /**
         * 是否是独占锁
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    /**
     * 加锁
     */
    @Override
    public void lock() {
        synchronizer.acquire(1);
    }

    /**
     * 加锁,可打断
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        synchronizer.acquireInterruptibly(1);
    }

    /**
     * 尝试加锁(只尝试一次),一次不成功则返回false
     */
    @Override
    public boolean tryLock() {
        return synchronizer.tryAcquire(1);
    }

    /**
     * 尝试加锁,带超时时间
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return synchronizer.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        synchronizer.release(1);
    }

    /**
     * 创建条件变量
     */
    @Override
    public Condition newCondition() {
        return synchronizer.newCondition();
    }
}
