package com.luangeng.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * Created by LG on 2017/9/23.
 */
public class ZkConfigService implements Watcher {

    private static ZkConfigService ser = new ZkConfigService();
    public final String CONFIG_PATH = "/__config__";
    private final Charset CHARSET = Charset.forName("UTF-8");
    private final int SESSION = 5000;
    private ZooKeeper zk;
    private CountDownLatch latch = new CountDownLatch(1);

    private ZkConfigService() {
        try {
            zk = new ZooKeeper("localhost:2181", SESSION, this);
            latch.await();
            if (zk.exists(CONFIG_PATH, false) == null) {
                zk.create(CONFIG_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public static ZkConfigService getInstance() {
        return ser;
    }

    private void close() throws InterruptedException {
        zk.close();
    }

    public void write(String path, String value) {
        path = CONFIG_PATH + "/" + path;
        Stat stat = null;
        try {
            stat = zk.exists(path, false);
            if (stat == null) {
                zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                zk.setData(path, value.getBytes(CHARSET), -1);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String read(String path, Watcher watcher) {
        path = CONFIG_PATH + "/" + path;
        byte[] data = new byte[0];
        try {
            data = zk.getData(path, watcher, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new String(data, CHARSET);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }
    }
}
