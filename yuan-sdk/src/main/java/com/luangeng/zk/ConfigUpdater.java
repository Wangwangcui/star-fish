package com.luangeng.zk;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by LG on 2017/9/23.
 */
public class ConfigUpdater {

    public static final String PATH = "/config";
    private Random random = new Random();
    private ActiveKeyValueStore store;

    public ConfigUpdater(String hosts) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore();
        store.connect(hosts);
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        ConfigUpdater up = new ConfigUpdater("localhost");
        up.run();
    }

    public void run() throws KeeperException, InterruptedException {
        while (true) {
            String value = random.nextInt(100) + "";
            store.write(PATH, value);
            System.out.printf("Set %s to %s\n", PATH, value);
            TimeUnit.SECONDS.sleep(random.nextInt(10));
        }
    }
}
