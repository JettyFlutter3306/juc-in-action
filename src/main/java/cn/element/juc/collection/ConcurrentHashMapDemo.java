package cn.element.juc.collection;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 线程安全集合类的使用
 * Java并发包下主要有三种线程安全集合类
 * Blocking大部分实现基于锁,并提供用来阻塞的方法
 * CopyOnWrite之类容器修改开销相对较重
 * Concurrent类型的容器内部很多操作使用CAS优化,一般可以提供较高的吞吐量
 *           1.遍历时弱一致性,例如使用迭代器遍历时,如果容器发生修改,迭代器
 *           仍然可以继续进行遍历,这时候内容时旧的
 *           2.求大小弱一致性,size()操作未必是百分之百准确
 *           3.读取弱一致性
 */
public class ConcurrentHashMapDemo {

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 生成字母文件
     */
    public static void createAlphabetFiles() {
        int length = alphabet.length();
        int count = 200;
        List<String> list = new ArrayList<>(length * count);

        for (int i = 0; i < length; i++) {
            char ch = alphabet.charAt(i);

            for (int j = 0; j < count; j++) {
                list.add(ch + "");
            }
        }

        Collections.shuffle(list);

        for (int i = 0; i < 26; i++) {
            String url = "src/main/resources/alphabet/" + (i + 1) + ".txt";

            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(url)))) {
                String temp = list.subList(i * count, (i + 1) * count)
                        .stream()
                        .collect(Collectors.joining("\n"));

                pw.print(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 计算字母出现的次数
     * @param supplier          映射表生成器
     * @param consumer          映射表,字符串列表消费器
     * @param <V>               次数
     */
    public static <V> void computeTimes(Supplier<Map<String, V>> supplier, BiConsumer<Map<String, V>, List<String>> consumer) {
        Map<String, V> map = supplier.get();
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= 26; i++) {
            int index = i;

            Thread t = new Thread(() -> {
                List<String> words = readFormFiles(index);
                consumer.accept(map, words);
            });

            threads.add(t);
        }

        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(map);
    }

    private static List<String> readFormFiles(int index) {
        BufferedReader br;
        List<String> list = new ArrayList<>();

        try {
            String url = "src/main/resources/alphabet/" + index + ".txt";
            br = new BufferedReader(new FileReader(url));
            String s;

            while ((s = br.readLine()) != null) {
                list.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void main(String[] args) {
//        createAlphabetFiles();
        computeTimes(
                (Supplier<Map<String, LongAdder>>) ConcurrentHashMap::new,
                (map, words) -> {
                    for (String word : words) {
                        //如果缺少一个key,则计算生成一个值,然后将key value放入map
                        LongAdder value = map.computeIfAbsent(word, key -> new LongAdder());

                        //执行累加
                        value.increment();
                    }
                });
    }
}
