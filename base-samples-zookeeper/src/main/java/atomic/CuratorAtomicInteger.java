package atomic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import utils.ClientFactory;

/**
 * Created by HUXU on 2017/7/26.
 * 计数器是用来计数的, 利用ZooKeeper可以实现一个集群共享的计数器。 只要使用相同的path就可以得到最新的计数器值，
 * 这是由ZooKeeper的一致性保证的。Curator有两个计数器， 一个是用int来计数，一个用long来计数。
 */
public class CuratorAtomicInteger {

    /** zookeeper地址 */
    private static CuratorFramework client = ClientFactory.newClient();
    /** session超时时间 */
    static final int SESSION_OUTTIME = 5000;//ms

    public static void main(String[] args) throws Exception {

        //3 开启连接
        client.start();
        //cf.delete().forPath("/super");


        //4 使用DistributedAtomicInteger
        DistributedAtomicInteger atomicIntger =
                new DistributedAtomicInteger(client, "/super", new RetryNTimes(3, 1000));

        AtomicValue<Integer> value = atomicIntger.add(1);
        System.out.println(value.succeeded());
        System.out.println(value.postValue());  //最新值
        System.out.println(value.preValue());   //原始值

    }

}
