package cn.element.juc.flyweight;

import java.sql.Connection;
import java.util.Random;

public class Context {

    public static void main(String[] args) {
        ConnectionPool pool = new ConnectionPool(2);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Connection connection = pool.getConnection();

                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                pool.free(connection);
            }).start();
        }
    }
}
