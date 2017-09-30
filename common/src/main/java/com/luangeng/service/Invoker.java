package com.luangeng.service;

/**
 * Created by LG on 2017/9/29.
 */
public interface Invoker {
    String invoke(String msg);

    String invoke(Process p);
}
