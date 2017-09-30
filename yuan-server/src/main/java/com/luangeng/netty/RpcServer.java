package com.luangeng.netty;

import com.luangeng.bean.*;
import com.luangeng.support.IPUtil;
import com.luangeng.zk.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by LG on 2017/9/28.
 */
public class RpcServer implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    // 存放接口名与服务对象之间的映射关系
    private static Map<String, Object> handlerMap = new HashMap<>();
    private String serverIp;
    private String serverPort;
    private ServiceRegistry serviceRegistry;


    public RpcServer(String serverPort) {
        this.serverPort = serverPort;
    }

    public RpcServer(String serverPort, ServiceRegistry serviceRegistry) {
        this.serverPort = serverPort;
        this.serviceRegistry = serviceRegistry;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
                                    .addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
                                    .addLast(new RpcHandler(handlerMap)); // 处理 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverIp = IPUtil.getLoaclIP();
            int port = Integer.parseInt(serverPort);

            ChannelFuture future = bootstrap.bind(serverIp, port).sync();
            LOGGER.info("server started on port {}", port);

            if (serviceRegistry != null) {
                serviceRegistry.register(serverIp + ":" + serverPort);
            }

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


        // 获取所有带有 RpcService 注解的 Spring Bean
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean : serviceBeanMap.values()) {
            String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
            handlerMap.put(interfaceName, serviceBean);
        }
    }
}