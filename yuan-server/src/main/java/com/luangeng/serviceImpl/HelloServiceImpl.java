package com.luangeng.serviceImpl;


import com.luangeng.bean.RpcService;
import com.luangeng.service.HelloService;

/**
 * Created by LG on 2017/9/28.
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}
