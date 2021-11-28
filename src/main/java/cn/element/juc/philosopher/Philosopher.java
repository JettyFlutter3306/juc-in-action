package cn.element.juc.philosopher;

import lombok.extern.slf4j.Slf4j;

/**
 * 问题描述：一圆桌前坐着5位哲学家，两个人中间有一只筷子，桌子中央有面条。
 * 哲学家思考问题，当饿了的时候拿起左右两只筷子吃饭，必须拿到两只筷子才能吃饭。
 * 上述问题会产生死锁的情况，当5个哲学家都拿起自己右手边的筷子，准备拿左手边的筷子时产生死锁现象。
 *
 * 解决办法：
 * 　　每个哲学家必须确定自己左右手的筷子都可用的时候，才能同时拿起两只筷子进餐，吃完之后同时放下两只筷子。
 */
@Slf4j(topic = "c.Philosopher")
public class Philosopher extends Thread {

    private final Chopsticks left;  //表示左手边的筷子

    private final Chopsticks right;  //表示右手边的筷子

    public Philosopher(String name, Chopsticks left, Chopsticks right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    /**
     * 到最后的情况可能是每个哲学家各拿着一根儿筷子等待其他哲学家
     * 释放筷子,从而陷入循环等待,也就是形成死锁
     * 可以考虑使用ReentrantLock来避免死锁
     */
    @Override
    public void run() {
        while (true) {
            //尝试获得左手边的筷子
            if (left.tryLock()) {
                try {
                    //尝试获取右手边的筷子
                    if (right.tryLock()) {
                        try {
                            //左手边和右手边的筷子都拿到了就吃饭
                            eat();
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();  //释放左手边的筷子
                }
            }
        }
    }

    private void eat() {
        log.debug("eating....");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
