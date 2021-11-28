package cn.element.juc.mq;

public class Context {

    public static void main(String[] args) {
        MessageQueue mq = new MessageQueue(2);

        //创建生产者
        for (int i = 0; i < 3; i++) {
            int id = i;

            new Thread(() -> {
                mq.put(new Message(id, "值: " + id));
            }, "生产者: " + i).start();
        }

        //创建消费者
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = mq.take();
            }
        }, "消费者").start();
    }
}
