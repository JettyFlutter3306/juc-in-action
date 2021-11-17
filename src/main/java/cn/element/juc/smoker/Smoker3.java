package cn.element.juc.smoker;

public class Smoker3 extends Thread {

    private final Table table;

    public Smoker3(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            if (table.isOffer3()) {
                smoke();  //吸烟

                table.end();  //吸烟结束 end()方法中设置标志位true
            }
        }
    }

    private void smoke() {
        System.out.println("烟民 3 号得到了 [胶水] [烟草],开始吸烟!");
    }
}
