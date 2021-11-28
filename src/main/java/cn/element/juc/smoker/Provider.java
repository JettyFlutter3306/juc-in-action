package cn.element.juc.smoker;

/**
 * 资源供应者
 */
public class Provider extends Thread {

    private final Table table;

    public Provider(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            while (table.isFinished()) {
                table.provide();
                table.setFinished(false);  //提供资源后吸烟开始 设为false
            }
        }
    }
}
