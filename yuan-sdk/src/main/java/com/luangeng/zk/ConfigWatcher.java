package com.luangeng.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * Created by LG on 2017/9/23.
 */
public class ConfigWatcher implements Watcher {

    private ActiveKeyValueStore store;

    public ConfigWatcher(String hosts) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore();
        store.connect(hosts);
    }

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
        ConfigWatcher w = new ConfigWatcher("localhost");
        w.display();

        Thread.sleep(Long.MAX_VALUE);
    }

    public void display() throws KeeperException, InterruptedException {
        String value = store.read(ConfigUpdater.PATH, this);
        System.out.printf("Read %s as %s\n", ConfigUpdater.PATH, value);
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
