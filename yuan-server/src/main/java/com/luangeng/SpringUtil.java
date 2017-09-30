package com.luangeng;

import org.springframework.context.ApplicationContext;

/**
 * Created by LG on 2017/9/29.
 */
public class SpringUtil {

    private static ApplicationContext context;


    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }


}
