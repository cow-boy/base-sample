package Barrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import utils.ClientFactory;

/**
 * Created by HUXU on 2017/7/26.
 * 分布式Barrier是这样一个类： 它会阻塞所有节点上的等待进程，知道某一个被满足， 然后所有的节点继续进行。
 * 比如赛马比赛中， 等赛马陆续来到起跑线前。 一声令下，所有的赛马都飞奔而出。
 */
public class BarrierDemo {

    private static CuratorFramework client = ClientFactory.newClient();

    public static void main(String[] args) throws Exception {

        client.start();

        //创建屏障类 不同JVM需要使用相同的目录 即/DistributedBarrier
        final DistributedBarrier barrier = new DistributedBarrier(client, "/DistributedBarrier");
        //创建屏障节点
        barrier.setBarrier();

        //启动一个线程，5000毫秒后移除屏障
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    barrier.removeBarrier();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //等待屏障移除
        barrier.waitOnBarrier();
        System.out.println("======屏障已经移除======");

        Thread.sleep(30000);
        client.close();
    }

}
