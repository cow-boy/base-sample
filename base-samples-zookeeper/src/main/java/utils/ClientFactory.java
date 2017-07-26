package utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * Created by HUXU on 2017/7/25.
 */
public class ClientFactory {

    public static CuratorFramework newClient() {
        String connectionString = "192.168.71.92:2181,192.168.71.93:2181,192.168.71.94:2181";
        //重试5次，每次间隔时间指数增长(有具体增长公式)
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 5);
        //重试5次，每次间隔5秒
        RetryPolicy retryNTimes = new RetryNTimes(5, 5000);
        //重试2分钟，每次间隔5秒,//普通创建
        RetryPolicy retryUntilElapsed = new RetryUntilElapsed(60000 * 2, 5000);
        //建立连接
        return CuratorFrameworkFactory.newClient(connectionString, 500, 500, retry);
    }

}
