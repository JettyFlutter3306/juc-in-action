package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 测试偏向锁
 */
@Slf4j(topic = "c.BiasedDemo")
public class BiasedDemo {
    
    static class Dog {
        private String name;
        private String color;

        public Dog() {
        }

        public Dog(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public static void main(String[] args) {
        Dog dog = new Dog("高飞", "黄色");

        log.debug("{}", ClassLayout.parseInstance(dog).toPrintable(dog));

    }

}
