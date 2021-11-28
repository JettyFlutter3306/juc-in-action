package cn.element.juc.philosopher;

import java.util.concurrent.locks.ReentrantLock;

public class Chopsticks extends ReentrantLock {

    private String name;

    public Chopsticks(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chopsticks{" +
                "name='" + name + '\'' +
                '}';
    }
}
