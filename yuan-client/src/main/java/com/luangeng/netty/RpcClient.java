package com.luangeng.netty;

import com.luangeng.bean.RpcDecoder;
import com.luangeng.bean.RpcEncoder;
import com.luangeng.bean.RpcRequest;
import com.luangeng.bean.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by LG on 2017/9/28.
 */
public class RpcClient {

    private static Channel c;

    public static Channel connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline()
                        .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                        .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                        .addLast(new ClientHandle()); // 使用 RpcClient 发送 RPC 请求
            }
        });
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = null;
        try {
            future = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c = future.channel();
        return c;
    }

    public void close() {
        //group.shutdownGracefully();
    }
}
