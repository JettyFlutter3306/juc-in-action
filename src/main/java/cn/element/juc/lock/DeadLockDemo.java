package cn.element.juc.lock;

/**
 * 死锁案例
 */
public class DeadLockDemo {

    private static final String A = "A";
    private static final String B = "B";

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (A) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (B) {
                    System.out.println("1");
                }
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (B) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (A) {
                    System.out.println("2");
                }
            }
        }, "t2").start();
    }
}
