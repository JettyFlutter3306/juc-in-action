package cn.element.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * JUC并发包提供了
 *  AtomicBoolean
 *  AtomicInteger
 *  AtomicLong
 * 等原子类
 */
public class AtomicIntegerDemo {

    /**
     * 以AtomicInteger为例
     */
    public static void main(String[] args) {
//        AtomicInteger i = new AtomicInteger(0);

//        System.out.println(i.incrementAndGet());  // => ++i  1
//        System.out.println(i.getAndIncrement());  // => i++  1
//        System.out.println(i.decrementAndGet());  // => --i  1
//        System.out.println(i.getAndDecrement());  // => i--  1
//        System.out.println(i.getAndAdd(5));  // => i => i += 5 => 0
//        System.out.println(i.addAndGet(5));  // => i += 5 => 10

        AtomicInteger j = new AtomicInteger(5);

        System.out.println(j.updateAndGet(x -> x * 10));  //j *= 10


    }

}
