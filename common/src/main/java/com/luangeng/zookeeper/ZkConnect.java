package com.luangeng.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by LG on 2017/10/1.
 */
public class ZkConnect {

    private static ZooKeeper zk;

    private static CountDownLatch latch = new CountDownLatch(1);

    public static ZooKeeper connect() {
        return connect("localhost");
    }

    public static ZooKeeper connect(String address) {
        if (zk != null) {
            return zk;
        }

        try {
            zk = new ZooKeeper("localhost", 30000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return zk;
    }
}
