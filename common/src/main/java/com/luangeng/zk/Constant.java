package com.luangeng.zk;

/**
 * Created by LG on 2017/9/28.
 */
public interface Constant {

    int ZK_SESSION_TIMEOUT = 50000;

    String ZK_REGISTRY_PATH = "/registry";

    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

}
