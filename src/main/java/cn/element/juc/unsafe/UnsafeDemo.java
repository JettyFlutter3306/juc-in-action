package cn.element.juc.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe这个类偏向底层,直接操作内存和线程,不建议开发人员直接使用
 */
public class UnsafeDemo {

    static class Teacher {
        volatile int id;
        volatile String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Teacher{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);

        Unsafe unsafe = (Unsafe) field.get(null);
        System.out.println(unsafe);

        //1.获取域的偏移地址
        long idOffSet = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOffSet = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        Teacher teacher = new Teacher();

        //2.执行CAS操作
        unsafe.compareAndSwapInt(teacher, idOffSet, 0, 1);
        unsafe.compareAndSwapObject(teacher, nameOffSet, null, "张三");

        //3.验证
        System.out.println(teacher);


    }
}
