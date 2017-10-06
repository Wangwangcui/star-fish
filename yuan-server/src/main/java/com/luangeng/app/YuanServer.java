package com.luangeng.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class YuanServer {

    private static Logger logger = LoggerFactory.getLogger(YuanServer.class);

    public static void main(String[] args) {
        logger.info("Hello World!");

        new ClassPathXmlApplicationContext("spring.xml");
    }
}
