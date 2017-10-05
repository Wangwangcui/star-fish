package com.luangeng.serviceImpl;


import com.luangeng.bean.RpcService;
import com.luangeng.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * Created by LG on 2017/9/28.
 */
@RpcService(HelloService.class)
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}
