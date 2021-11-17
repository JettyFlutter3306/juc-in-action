package cn.element.juc.smoker;

public class Client {

    public static void main(String[] args) {

        Table table = new Table();

        Provider provider = new Provider(table);

        Smoker1 smoker1 = new Smoker1(table);
        Smoker2 smoker2 = new Smoker2(table);
        Smoker3 smoker3 = new Smoker3(table);

        provider.start();
        smoker1.start();
        smoker2.start();
        smoker3.start();

    }
}
