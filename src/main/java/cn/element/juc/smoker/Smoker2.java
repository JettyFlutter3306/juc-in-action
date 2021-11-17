package cn.element.juc.smoker;

public class Smoker2 extends Thread {

    private final Table table;

    public Smoker2(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            if (table.isOffer2()) {
                smoke();  //吸烟

                table.end();  //吸烟结束 end()方法中设置标志位true
            }
        }
    }

    private void smoke() {
        System.out.println("烟民 2 号得到了 [纸] [胶水],开始吸烟!");
    }
}
