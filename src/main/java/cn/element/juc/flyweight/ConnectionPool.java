package cn.element.juc.flyweight;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 使用享元模式模拟自定义数据库连接池
 */
@Slf4j(topic = "c.ConnectionPool")
public class ConnectionPool {

    //1.连接池大小
    private final int poolSize;

    //2.连接对象的数组
    private final Connection[] connections;

    //3.连接状态数组 0: 空闲 1: 繁忙  使用原子数组对状态进行一个保护
    private final AtomicIntegerArray states;

    //4.构造方法
    public ConnectionPool(int poolSize) {
        this.poolSize = poolSize;
        this.connections = new Connection[poolSize];
        this.states = new AtomicIntegerArray(new int[poolSize]);

        for (int i = 0; i < poolSize; i++) {
            connections[i] = new MockConnection("连接: " + (i + 1));
        }
    }

    //5.获取连接的方法
    public Connection getConnection() {
        while (true) {
            for (int i = 0; i < poolSize; i++) {
                //获取空闲连接
                if (states.get(i) == 0) {
                    //必须要等CAS成功才可以返回
                    if (states.compareAndSet(i, 0, 1)) {
                        log.debug("get {}", connections[i]);
                        return connections[i];
                    }
                }
            }

            //如果没有空闲连接,让当前线程进入等待状态
            synchronized (this) {
                try {
                    log.debug("waiting....");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //6.释放连接
    public void free(Connection connection) {
        for (int i = 0; i < poolSize; i++) {
            if (connections[i] == connection) {
                states.set(i, 0);

                //唤醒当前阻塞的所有得线程
                synchronized (this) {
                    log.debug("free {}", connection);
                    notifyAll();
                }

                break;
            }
        }
    }
}

