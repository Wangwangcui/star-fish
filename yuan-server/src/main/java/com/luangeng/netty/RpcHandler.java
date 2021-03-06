package com.luangeng.netty;

import com.luangeng.app.SpringUtil;
import com.luangeng.bean.RpcRequest;
import com.luangeng.bean.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LG on 2017/9/28.
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcHandler.class);

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) throws Exception {

        RpcResponse response = new RpcResponse();
        System.out.println(request.getRequestId() + ":" + request.getClassName());
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = SpringUtil.getImpl(className);

        Class serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}
