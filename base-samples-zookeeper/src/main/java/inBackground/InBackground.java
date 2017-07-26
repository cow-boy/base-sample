package inBackground;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;
import utils.ClientFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by HUXU on 2017/7/25.
 * 绑定回调函数
 */
public class InBackground {

    private static CuratorFramework client = ClientFactory.newClient();

    public static void main(String[] args) {

        try {
            client.start();

            ExecutorService es = Executors.newFixedThreadPool(5);//异步操作线程池,
            //异步判断操作
            Stat s1 = client.checkExists().inBackground().forPath("/curator1/test"); //无回调
            client.checkExists().inBackground(new BackgroundCallback(){   //有回调
                @Override
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                    CuratorEventType c = curatorEvent.getType();//事件类型，可在CuratorEventType看到具体种类
                    int r = curatorEvent.getResultCode();//0,执行成功，其它，执行失败
                    Object o = curatorEvent.getContext();//事件上下文，一般是由调用方法传入，供回调函数使用的参数
                    String p = curatorEvent.getPath();//节点路径
                    List<String> li = curatorEvent.getChildren();//子节点列表
                    byte[] datas = curatorEvent.getData();//节点数据
                }
            },es).forPath("/curator1/test");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }



}
