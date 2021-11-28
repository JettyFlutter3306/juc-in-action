package cn.element.juc.philosopher;

public class Context {

    public static void main(String[] args) {
        Chopsticks c1 = new Chopsticks("1");
        Chopsticks c2 = new Chopsticks("2");
        Chopsticks c3 = new Chopsticks("3");
        Chopsticks c4 = new Chopsticks("4");
        Chopsticks c5 = new Chopsticks("5");

        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("亚里士多德", c2, c3).start();
        new Philosopher("阿基米德", c3, c4).start();
        new Philosopher("柏拉图", c4, c5).start();
        new Philosopher("毕达哥拉斯", c5, c1).start();

    }
}
