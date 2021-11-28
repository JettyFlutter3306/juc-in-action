package cn.element.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "c.GuardedObject")
public class GuardedObjectDemo {

    static class GuardedObject {
        //结果
        private Object response;

        //timeout表示超时时间
        public Object get(long timeout) {
            synchronized (this) {
                //开始的时间
                long start = System.currentTimeMillis();

                //经历的时间
                long passed = 0;

                while (response == null) {
                    //经历的时间超过了最大等待时间就退出循环
                    if (passed >= timeout) {
                        break;
                    }

                    try {
                        this.wait(timeout - passed);  //还需要等待这么多时间
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    passed = System.currentTimeMillis() - start;
                }
            }

            return response;
        }

        //产生结果
        public void complete(Object response) {
            synchronized (this) {
                //给结果成员变量赋值
                this.response = response;
                this.notifyAll();
            }
        }
    }

    static class DownLoader {
        public static List<String> download() throws IOException {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://www.baidu.com/").openConnection();

            List<String> lines = new ArrayList<>();

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;

                while ((line = bf.readLine()) != null) {
                    lines.add(line);
                }
            }

            return lines;
        }
    }

    public static void main(String[] args) {
        GuardedObject guarded = new GuardedObject();

        new Thread(() -> {
            log.debug("等待结果...");
            List<String> list = (List<String>) guarded.get(2000);
            log.debug("页面源代码行数: {}", list.size());
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行下载...");

            try {
                //产生结果
                List<String> list = DownLoader.download();

                //传递结果
                guarded.complete(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2").start();

    }




}


