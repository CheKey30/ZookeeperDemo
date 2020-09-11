import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtil {
    private static ZooKeeper zk;
    // add zookeeper cluster addresses, you can add a /xxx to make it as the root
    private static String adderss = "192.168.150.11:2181,192.168.150.12:2181,192.168.150.13:2181/testConfig";
    private static DefaultWatch watch = new DefaultWatch();
    //the creation of zk is asyn, use a latch to make user it can be used
    private static CountDownLatch init = new CountDownLatch(1);
    public static ZooKeeper getZk(){
        try{
            zk = new ZooKeeper(adderss,1000,watch);
            watch.setCc(init);
            init.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        return zk;
    }

}
