package com.luangeng;

import com.luangeng.bean.RpcRequest;
import com.luangeng.bean.RpcResponse;
import com.luangeng.zk.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by LG on 2017/9/28.
 */
public class RpcProxy implements InvocationHandler {

    private static int i = 5;
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
        request.setRequestId(i++ + "");
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应
        RpcResponse r = client.invoke(request);
        if (r.getError() == null) {
            return r.getResult();
        }
        throw r.getError();
    }
}
