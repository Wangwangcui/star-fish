package com.luangeng.app;


import com.luangeng.RpcProxy;
import com.luangeng.service.HelloService;
import com.luangeng.service.Invoker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动类
 */
public class YuanClient {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcProxy rpcProxy = context.getBean(RpcProxy.class);

        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("Hello World!");
        System.out.println(result);

        Invoker invoker = rpcProxy.create(Invoker.class);
        String re = invoker.invoke("sss");
        System.out.println(re);
    }
}
