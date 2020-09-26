package zkLock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {
    ZooKeeper zk;

    String threadName;

    CountDownLatch countDownLatch = new CountDownLatch(1);

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    String pathName;

    public ZooKeeper getZk(){
        return zk;
    }

    public void setZk(ZooKeeper zk){
        this.zk = zk;
    }

    public void tryLock(){
        try {
            
            zk.create("/Lock",threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this,"context");
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public void  unlock(){
        try {
            zk.delete(pathName,-1);
            System.out.println(threadName+"finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void processResult(int i, String s, Object o, String s1) {
        if(s1!=null){
            pathName = s1;
            System.out.println(threadName+ "create node: "+ pathName);
            zk.getChildren("/",false,this,"context");
        }

    }

    public void process(WatchedEvent watchedEvent) {

        // if the first thread release the lock, the second thread can receive the callback event from zk
        // if the k thread in this queue dead, the k+1 thread can receive a callback event to monitor the k-1 thread's node in zk.

        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this,"context");
                break;
            case NodeDataChanged:
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

    public void processResult(int rc, String s, Object o, List<String> list, Stat stat) {

        // thread at here can see all node in front of itself
//        System.out.println("look locks");
//        for(String child: list){
//            System.out.println(child);
//        }
        Collections.sort(list);
        //check whether it is the first
        int i = list.indexOf(pathName.substring(1));
        if(i==0){
            //is the first
            System.out.println(threadName+"is the first");
            countDownLatch.countDown();
        }else{
            //not the first
            zk.exists("/"+list.get(i-1),this,this,"context");
        }

    }

    public void processResult(int i, String s, Object o, Stat stat) {

    }
}
