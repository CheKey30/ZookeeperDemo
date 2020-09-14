import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {


    ZooKeeper zk;
    MyConf conf;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if(bytes != null){
            String content = new String(bytes);
            conf.setConf(content);
            countDownLatch.countDown();
        }

    }

    public void processResult(int i, String s, Object o, Stat stat) {
        if(stat != null){
            zk.getData("/AppConf",this,this,"context");
        }

    }

    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()){

            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf",this,this,"context");
                break;
            case NodeDeleted:
                // tolerant
                conf.setConf("");
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData("/AppConf",this,this,"context");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }

    }

    public void aWait(){
        /*
         * 1. node does not exist
         * 2. node exists
         */
        zk.exists("/AppConf", this, this, "context");
        try{
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public MyConf getConf() {
        return conf;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

}
