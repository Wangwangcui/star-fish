package com.luangeng.netty;

import com.luangeng.bean.RpcRequest;
import com.luangeng.bean.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LG on 2017/10/5.
 */
public class ClientHandle extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);
    private final Object obj = new Object();
    private RpcResponse response;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        response = response;

        synchronized (obj) {
            obj.notify(); // 收到响应，唤醒线程
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }

    public RpcResponse invoke(RpcRequest request) throws InterruptedException {
        c.writeAndFlush(request).sync();
        synchronized (obj) {
            obj.wait();
        }
        c.closeFuture().sync();
        return response;
    }

}
