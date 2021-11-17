package cn.element.juc.smoker;

/**
 * 吸烟者问题
 *
 * 问题描述：
 * 三个吸烟者在一个房间内，还有一个香烟供应者。
 * 为了制造并抽掉香烟，每个吸烟者需要三样东西：烟草、纸和胶水，供应 者有丰富货物提供。
 * 三个吸烟者中，第一个有自己的胶水，第二个有自己的烟草，第三个有自己的纸。
 * 供应者随机地将两样东西放在桌子上，允许一个吸烟者进行对健康不利的吸烟。
 * 当吸烟者完成吸烟后唤醒供应者，供应者再把两样东西放在桌子上，唤醒另一个吸烟者。
 */
public class Table {

    private boolean isFinished = true;  //表示吸烟是否结束
    private boolean offer1 = false;  //信号量1 表示吸烟者1是否可以吸烟
    private boolean offer2 = false;  //信号量2
    private boolean offer3 = false;  //信号量3

    public synchronized void provide() {
        int random = (int) (Math.random() * 3);

        if (random == 0) {
            System.out.println("烟民 1 号是否可以开始吸烟: " + offer1);
            offer1 = true;
            System.out.println("提供了 [烟草] 和 [纸]");
        } else if (random == 1) {
            System.out.println("烟民 2 号是否可以开始吸烟: " + offer2);
            offer2 = true;
            System.out.println("提供了 [纸] 和 [胶水]");
        } else {
            System.out.println("烟民 3 号是否可以开始吸烟: " + offer3);
            offer3 = true;
            System.out.println("提供了 [胶水] 和 [烟草]");
        }
    }

    public synchronized void end() {
        if (offer1) {
            offer1 = false;  //吸烟者不能连续多次抽烟
            System.out.println("烟民 1 号吸烟结束");
            System.out.println("--------------------");
            setFinished(true);  //吸烟结束 可以开始下一次随机分配资源
        }

        if (offer2) {
            offer2 = false;  //吸烟者不能连续多次抽烟
            System.out.println("烟民 2 号吸烟结束");
            System.out.println("--------------------");
            setFinished(true);  //吸烟结束 可以开始下一次随机分配资源
        }

        if (offer3) {
            offer3 = false;  //吸烟者不能连续多次抽烟
            System.out.println("烟民 3 号吸烟结束");
            System.out.println("--------------------");
            setFinished(true);  //吸烟结束 可以开始下一次随机分配资源
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isOffer1() {
        return offer1;
    }

    public void setOffer1(boolean offer1) {
        this.offer1 = offer1;
    }

    public boolean isOffer2() {
        return offer2;
    }

    public void setOffer2(boolean offer2) {
        this.offer2 = offer2;
    }

    public boolean isOffer3() {
        return offer3;
    }

    public void setOffer3(boolean offer3) {
        this.offer3 = offer3;
    }
}
