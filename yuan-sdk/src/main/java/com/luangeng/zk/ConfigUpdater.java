package com.luangeng.zk;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by LG on 2017/9/23.
 */
public class ConfigUpdater {

    private String path;
    private Random random = new Random();

    private ZkConfigService configSer = ZkConfigService.getInstance();

    public ConfigUpdater(String path) {
        this.path = path;
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        ConfigUpdater up = new ConfigUpdater("db.url");
        up.run();
    }

    public void run() throws KeeperException, InterruptedException {
        int i = 0;
        while (true) {
            String value = i++ + "";
            configSer.write(path, value);
            System.out.printf("Set %s to %s\n", path, value);
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        }
    }
}
