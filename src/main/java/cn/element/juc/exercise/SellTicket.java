package cn.element.juc.exercise;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 使用多线程模拟买票问题
 */
@Slf4j(topic = "c.Ticket")
public class SellTicket {

    //Random为线程安全
    static Random random = new Random();

    //随机1~5
    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }

    static class TicketWindow {
        private int count;

        public TicketWindow(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        /**
         * 给sell()方法上锁,避免线程安全问题
         */
        public synchronized int sell(int amount) {
            if (this.count >= amount) {
                this.count -= amount;
                return amount;
            } else {
                return 0;
            }
        }
    }

    /**
     * 其实会出现线程安全问题
     * 会出现1000张票会卖出1002张的问题
     * 69讲
     */
    public static void main(String[] args) {
        TicketWindow window = new TicketWindow(1000);
        List<Thread> list = new ArrayList<>();

        //用来存储卖出去多少张票
        List<Integer> sellCount = new Vector<>();

        for (int i = 0; i < 2000; i++) {
            Thread t = new Thread(() -> {
                //分析这里的竞争条件
                int count = window.sell(randomAmount());

                //统计卖票数
                sellCount.add(count);
            });

            list.add(t);
            t.start();
        }

        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //卖出去的票求和
        log.debug("tickets sold: {}", sellCount.stream().reduce(0, Integer::sum));

        //剩余票数
        log.debug("tickets remained: {}", window.getCount());
    }
}
