package cn.element.juc.pool;

@FunctionalInterface
public interface RejectStrategy<T> {

    void reject(IBlockingQueue<T> queue, T task);
}
