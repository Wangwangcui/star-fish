package com.luangeng.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * Created by LG on 2017/9/23.
 */
public class ConfigWatcher implements Watcher {

    private String path;

    private ZkConfigService configSer = ZkConfigService.getInstance();

    public ConfigWatcher(String path) {
        this.path = path;
    }

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
        ConfigWatcher w = new ConfigWatcher("db.url");
        w.display();

        Thread.sleep(Long.MAX_VALUE);
    }

    public void display() throws KeeperException, InterruptedException {
        String value = configSer.read(path, this);
        System.out.printf("Read %s as %s\n", path, value);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDataChanged) {
            try {
                display();
            } catch (InterruptedException e) {
                System.err.println(e);
                Thread.currentThread().interrupt();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
    }
}
