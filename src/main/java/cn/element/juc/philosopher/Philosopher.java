package cn.element.juc.philosopher;

/**
 * 问题描述：一圆桌前坐着5位哲学家，两个人中间有一只筷子，桌子中央有面条。
 * 哲学家思考问题，当饿了的时候拿起左右两只筷子吃饭，必须拿到两只筷子才能吃饭。
 * 上述问题会产生死锁的情况，当5个哲学家都拿起自己右手边的筷子，准备拿左手边的筷子时产生死锁现象。
 *
 * 解决办法：
 *
 * 　　每个哲学家必须确定自己左右手的筷子都可用的时候，才能同时拿起两只筷子进餐，吃完之后同时放下两只筷子。
 */
public class Philosopher extends Thread {

    private Integer index;

    private final String name;

    private final Chopsticks chopsticks;

    public Philosopher(Integer index, String name, Chopsticks chopsticks) {
        this.index = index;
        this.name = name;
        this.chopsticks = chopsticks;
    }

    @Override
    public void run() {

        think();  //思考

        chopsticks.takeStick(this);  //拿起筷子

        eat();  //吃饭

        chopsticks.putStick(this);  //放下筷子
    }

    public void think() {

        try {
            System.out.println(index + ": " + name + "在思考...");
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void eat() {

        try {
            System.out.println(index + ": " + name + "在吃饭...");
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "index=" + index +
                ", name='" + name + '\'' +
                '}';
    }
}
