package cn.element.juc.mq;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 定义一个在java线程之间通信的消息队列
 * 模仿rabbitmq
 */
@Slf4j(topic = "c.MessageQueue")
public class MessageQueue {

    private final Deque<Message> deque = new LinkedList<>();  //消息队列容器

    private final int capacity;  //容量

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    //获取消息
    public Message take() {
        //检查队列是否为空
        synchronized (deque) {
            while (deque.isEmpty()) {
                try {
                    log.debug("队列为空,消费者线程等待...");
                    deque.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //从队列头部获取消息返回
            Message message = deque.removeFirst();
            log.debug("已经消费消息: {}", message);
            deque.notifyAll();

            return message;
        }
    }

    public void put(Message message) {
        synchronized (deque) {
            //检查队列是否已满
            while (deque.size() == capacity) {
                try {
                    log.debug("队列已满,生产者线程等待...");
                    deque.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            deque.addLast(message);  //把消息加入到队列尾部
            log.debug("已经生产消息: {}", message);
            deque.notifyAll();  //唤醒等待的所有线程
        }
    }

}
