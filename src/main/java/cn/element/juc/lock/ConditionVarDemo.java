package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的多种条件变量
 */
@Slf4j(topic = "c.ConditionVar")
public class ConditionVarDemo {

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {
        //创建一个新的条件变量(休息室)
        Condition condition1 = reentrantLock.newCondition();
        Condition condition2 = reentrantLock.newCondition();

        reentrantLock.lock();

        //进入休息室等待
        try {
            condition1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        condition1.signal();  //
        condition1.signalAll();

    }


}
