package cn.element.juc.philosopher;

public class Client {

    public static void main(String[] args) {

        Chopsticks chopsticks = new Chopsticks();

        Philosopher philosopher1 = new Philosopher(0, "苏格拉底", chopsticks);
        Philosopher philosopher2 = new Philosopher(1, "亚里士多德", chopsticks);
        Philosopher philosopher3 = new Philosopher(2, "阿基米德", chopsticks);
        Philosopher philosopher4 = new Philosopher(3, "柏拉图", chopsticks);
        Philosopher philosopher5 = new Philosopher(4, "毕达哥拉斯", chopsticks);

        philosopher1.start();
        philosopher2.start();
        philosopher3.start();
        philosopher4.start();
        philosopher5.start();
    }
}
