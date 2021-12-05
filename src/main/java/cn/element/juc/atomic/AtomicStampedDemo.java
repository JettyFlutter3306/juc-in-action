package cn.element.juc.atomic;

import cn.element.juc.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j(topic = "c.AtomicStampedReference")
public class AtomicStampedDemo {

    static AtomicStampedReference<String> reference = new AtomicStampedReference<>("A", 0);

    private static void other() {
        new Thread(() -> {
            int stamp = reference.getStamp();
            log.debug("{}", stamp);
            log.debug("change A -> B {}", reference.compareAndSet(reference.getReference(), "B", stamp, stamp + 1));
        }, "t1").start();

        ThreadUtil.sleep(0.5);

        new Thread(() -> {
            int stamp = reference.getStamp();
            log.debug("{}", stamp);
            log.debug("change B -> A {}", reference.compareAndSet(reference.getReference(), "A", stamp, stamp + 1));
        }, "t2").start();
    }

    public static void main(String[] args) {
        log.debug("main start ...");

        //获取值A
        //这个共享变量被其他线程修改过?
        String prev = reference.getReference();
        int stamp = reference.getStamp();
        log.debug("{}", stamp);

        other();

        ThreadUtil.sleep(0.5);

        log.debug("{}", stamp);
        log.debug("change A -> C {}", reference.compareAndSet(prev, "C", stamp, stamp + 1));

    }
}
