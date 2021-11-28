package cn.element.juc.mq;

/**
 * 定义消息类
 */
public final class Message {

    private int id;

    private Object value;

    public Message() {

    }

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
