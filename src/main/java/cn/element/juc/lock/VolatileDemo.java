package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;

/**
 * Volatile关键字的使用
 *
 * JIT对经常访问的主存里数据会进行优化,会把主存里面的数据
 * 缓存到线程里面的告诉缓存,导致主存的数据被修改高速缓存的数据
 * 得不到同步修改,所以需要使用volatile关键字
 * 这样的话,线程就会直接从主存中获取数据
 */
@Slf4j(topic = "c.Volatile")
public class VolatileDemo {

    //易变
    volatile static boolean run = true;

    public static void main(String[] args) {
        new Thread(() -> {
            while (run) {
                //
            }
        }, "t1").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("停止t1...");
        run = false;
    }





}
