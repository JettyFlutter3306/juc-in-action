package cn.element.juc.unchangable;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * 可变类在多线程环境中可能会发生线程安全问题
 * 可以考虑使用不可变类来改进
 */
@Slf4j(topic = "c.SimpleDateDemo")
public class SimpleDateDemo {

    public static void testSimpleDateFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (formatter) {
                    try {
                        log.debug("{}", formatter.parse("1951-04-21"));
                    } catch (ParseException e) {
                        log.error("{}", e);
                    }
                }
            }).start();
        }
    }

    public static void testDateTimeFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                TemporalAccessor accessor = formatter.parse("1951-04-21");
                log.debug("{}", accessor);
            }).start();
        }
    }

    public static void main(String[] args) {
        testDateTimeFormatter();
    }
}