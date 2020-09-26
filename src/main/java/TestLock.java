import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import zkLock.WatchCallBack;

public class TestLock {
    ZooKeeper zk;
    @Before
    public void conn(){
        zk = ZKUtil.getZk();

    }

    @After
    public void close(){
        try {
            zk.close();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void lock(){
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run(){
                    // these 10 threads might run on ten different machines. So it is distributed lock
                    WatchCallBack watchCallBack = new WatchCallBack();
                    watchCallBack.setZk(zk);
                    String threadName = Thread.currentThread().getName();
                    watchCallBack.setThreadName(threadName);
                    // try to get the lock
                    watchCallBack.tryLock();
                    //do the job
                    System.out.println(threadName+"working");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // release the lock
                    watchCallBack.unlock();

                }
            }.start();
        }

        // infinity while to keep main thread running
        while (true){

        }

    }
}
