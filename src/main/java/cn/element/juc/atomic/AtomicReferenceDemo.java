package cn.element.juc.atomic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

    public static void main(String[] args) {
        DecimalAccount.run(new DecimalAccountCAS(new BigDecimal("10000")));
    }
}

class DecimalAccountCAS implements DecimalAccount {
    private final AtomicReference<BigDecimal> balance;

    public DecimalAccountCAS(BigDecimal balance) {
        this.balance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withDraw(BigDecimal amount) {
        while (true) {
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);

            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}

interface DecimalAccount {

    //获取余额
    BigDecimal getBalance();

    //取款
    void withDraw(BigDecimal amount);

    /**
     * 方法会启动1000个线程,每个线程做 -10 的操作
     * 如果初始化余额为 10000 ,那么正确的结果应该是0
     */
    static void run(DecimalAccount account) {
        List<Thread> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            list.add(new Thread(() -> {
                account.withDraw(BigDecimal.TEN);
            }));
        }

        list.forEach(Thread::start);
        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(account.getBalance());
    }


}
