package cn.element.juc.atomic;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicUpdaterDemo {

    /**
     * 属性必须得加上volatile关键字,否则会报异常
     */
    static class Student {
        volatile String name;


        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        Student student = new Student();

        AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater
                .newUpdater(Student.class, String.class, "name");

        if (updater.compareAndSet(student, null, "张三")) {
            System.out.println(student);
        }
    }
}
