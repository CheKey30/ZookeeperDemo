import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfig {
    ZooKeeper zk;

    @Before
    public void conn(){
        zk = ZKUtil.getZk();
    }

    @After
    public void close(){
        try{
            zk.close();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getConfig(){
        zk.exists("/AppConf", new Watcher() {
            public void process(WatchedEvent watchedEvent) {

            }
        })
    }
}
