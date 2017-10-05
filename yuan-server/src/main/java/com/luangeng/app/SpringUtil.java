package com.luangeng.app;

import com.luangeng.bean.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LG on 2017/9/29.
 */
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static Map<String, Object> handlerMap = new HashMap<>();

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static Object getImpl(String intface) {
        return handlerMap.get(intface);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        // 获取所有带有 RpcService 注解的 Spring Bean
        Map<String, Object> serviceBeanMap = context.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean : serviceBeanMap.values()) {
            String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
            handlerMap.put(interfaceName, serviceBean);
        }
    }

}
