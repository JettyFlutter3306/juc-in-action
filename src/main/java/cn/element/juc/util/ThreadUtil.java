package cn.element.juc.util;

public class ThreadUtil {

    public static void sleep(double i) {
        try {
            Thread.sleep((int) (i * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
