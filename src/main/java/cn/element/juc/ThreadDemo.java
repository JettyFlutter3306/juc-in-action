package cn.element.juc;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Sync")
public class ThreadDemo {

    static class Ticket {

        Integer count = 1000;
    }

    public static void main(String[] args) {

        Ticket ticket = new Ticket();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (ticket.count > 0) {
                    synchronized (ticket.count) {
                        ticket.count--;

                        System.out.println(ticket.count);
                    }
                }
            }).start();
        }


    }
}
