package cn.element.juc.philosopher;

public class Chopsticks {

    private final boolean[] captured = new boolean[5];

    public synchronized void takeStick(Philosopher philosopher) {

        Integer i = philosopher.getIndex();

        while (captured[i] || captured[(i + 1) % 5]) {
            try {
                wait();  //如果拿不到筷子,那么就一直陷入阻塞状态
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        captured[i] = true;
        captured[(i + 1) % 5] = true;
    }

    public synchronized void putStick(Philosopher philosopher) {

        Integer i = philosopher.getIndex();

        captured[i] = false;
        captured[(i + 1) % 5] = false;

        notifyAll();  //放下筷子后叫醒其他哲学家
    }


}
