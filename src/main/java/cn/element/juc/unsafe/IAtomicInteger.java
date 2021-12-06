package cn.element.juc.unsafe;

import sun.misc.Unsafe;

/**
 * 自定义AtomicInteger类
 * 需要使用Unsafe类
 */
public class IAtomicInteger implements Account {

    private volatile int value;

    private static final long valueOffset;

    private static final Unsafe UNSAFE;

    static {
        UNSAFE = UnsafeAccessor.getUnsafe();

        try {
            valueOffset = UNSAFE.objectFieldOffset(IAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public IAtomicInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void decrement(int amount) {
        while (true) {
            int prev = this.value;
            int next = prev - amount;

            if (UNSAFE.compareAndSwapInt(this, valueOffset, prev, next)) {
                break;
            }
        }
    }

    @Override
    public Integer getBalance() {
        return getValue();
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }

    public static void main(String[] args) {

    }
}

interface Account {

    Integer getBalance();

    void withdraw(Integer amount);
}
