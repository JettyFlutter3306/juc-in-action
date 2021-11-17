package cn.element.juc.smoker;

public class Smoker1 extends Thread {

    private final Table table;

    public Smoker1(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            if (table.isOffer1()) {
                smoke();  //吸烟

                table.end();  //吸烟结束 end()方法中设置标志位true
            }
        }
    }

    private void smoke() {
        System.out.println("烟民 1 号得到了 [烟草] [纸],开始吸烟!");
    }
}
