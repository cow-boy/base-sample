package com.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by HUXU on 2017/7/24.
 */
public class Curator {

    public static final String url = "192.168.71.232:2181,192.168.71.233:2181,192.168.71.234:2181";

    public static void main(String[] args) throws Exception {

        //重试5次，每次间隔时间指数增长(有具体增长公式)
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 5);
        //重试5次，每次间隔5秒
        RetryPolicy retryNTimes = new RetryNTimes(5, 5000);
        //重试2分钟，每次间隔5秒,//普通创建
        RetryPolicy retryUntilElapsed = new RetryUntilElapsed(60000 * 2, 5000);
        //建立连接
        CuratorFramework client = CuratorFrameworkFactory.newClient(url, 500, 500, retry);
        //fluent风格
       /* CuratorFramework client1 = CuratorFrameworkFactory.builder().connectString(url)
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(3000)
                .retryPolicy(retry)
                .build();*/
        //开启连接
        client.start();
        System.out.println("zk operation");
        //创建节点
       /* String path = client.create()
                //.creatingParentsIfNeeded()  //对节点路径上没有的节点进行创建
                .withMode(CreateMode.PERSISTENT) //临时、持久， 4中节点类型
                .forPath("/curator", "cowboy".getBytes());  //节点路径、节点值
        Thread.sleep(500);
        //String path = client.create().forPath("/head", new byte[0]);
        System.out.println(path);*/

       /* //删除节点
        client.delete()
                .guaranteed()      //删除失败，则客户端持续删除，直到节点删除为止
                .deletingChildrenIfNeeded()   //删除相关子节点
                .withVersion(-1)    //无视版本，直接删除
                .forPath("/test");
*/
        //更新节点信息
        Stat stat2 = new Stat();
        //byte[] theValue2 = client.getData().storingStatIn(stat2).forPath("/test");
        client.setData()
                //.withVersion(stat2.getVersion())  //版本校验，与当前版本不一致则更新失败,-1则无视版本信息进行更新
                .forPath("/test", "1456".getBytes());

        //判断节点是否存在(存在返回节点信息，不存在则返回null)
        Stat s = client.checkExists().forPath("/curator/test");
        System.out.println(s);

    }

}
