import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfig {
    ZooKeeper zk;
    MyConf myConf;

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
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        watchCallBack.setConf(myConf);

        while (true){
            if(myConf.getConf().equals("")){
                System.out.println("conf lost");
                watchCallBack.aWait();
            }else {
                System.out.println(myConf.getConf());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
