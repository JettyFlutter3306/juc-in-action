package cn.element.juc.atomic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AtomicArrayDemo {

    /**
     *
     * @param supplier      提供数组,可以是线程不安全数组或线程安全数组
     * @param function      获取数组长度的方法
     * @param biConsumer    自增方法,回传array,index
     * @param consumer      打印数组的方法
     */
    private static <T> void run(Supplier<T> supplier,
                                Function<T, Integer> function,
                                BiConsumer<T, Integer> biConsumer,
                                Consumer<T> consumer) {
        List<Thread> list = new ArrayList<>();
        T array = supplier.get();
        int length = function.apply(array);

        for (int i = 0; i < length; i++) {
            list.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    biConsumer.accept(array, j % length);
                }
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

        consumer.accept(array);
    }

    public static void main(String[] args) {
        run(() -> new int[10],
                array -> array.length,
                (array, index) -> array[index]++,
                array -> System.out.println(Arrays.toString(array)));

        run(() -> new AtomicIntegerArray(10),
                AtomicIntegerArray::length,
                AtomicIntegerArray::getAndIncrement,
                System.out::println);
    }
}
