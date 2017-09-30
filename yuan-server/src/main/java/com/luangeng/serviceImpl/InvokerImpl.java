package com.luangeng.serviceImpl;

import com.luangeng.bean.RpcService;
import com.luangeng.service.Invoker;

/**
 * Created by LG on 2017/9/29.
 */
@RpcService(InvokerImpl.class)
public class InvokerImpl implements Invoker {

    @Override
    public String invoke(String name) {
        return "invoke! " + name;
    }

    @Override
    public String invoke(Process p) {
        return p.toString();
    }
}
