package com.luangeng.support;

import com.luangeng.rs.WSHello;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Created by LG on 2017/9/23.
 */
public class WServer {

    private static int port = 8080;

    private static ResourceConfig resourceConfig = new ResourceConfig();

    private WServer() {
    }

    private static void register(Class clazz) {
        resourceConfig.registerClasses(clazz);
    }

    public static void init() {
        WServerThread t = new WServerThread();

        register(WSHello.class);

        t.start();
    }

    private static class WServerThread extends Thread {
        @Override
        public void run() {
            Server server = new Server();
            SelectChannelConnector serverConnector = new SelectChannelConnector();
            serverConnector.setPort(port);

            ServletContainer servletContainer = new ServletContainer(resourceConfig);
            server.addConnector(serverConnector);

            ServletContextHandler servletContextHandler = new ServletContextHandler();
            servletContextHandler.addServlet(new ServletHolder(servletContainer), "/*");
            server.setHandler(servletContextHandler);

            try {
                server.start();
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
