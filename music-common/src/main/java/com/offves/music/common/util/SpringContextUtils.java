package com.offves.music.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class SpringContextUtils {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        Assert.notNull(applicationContext, "current applicationContext is null");

        return applicationContext;
    }

    public static <T> List<T> getBeansOfType(Class<T> type) {
        return new ArrayList<>(getApplicationContext().getBeansOfType(type).values());
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static void setApplicationContextIfNone(ApplicationContext context) {
        if (applicationContext == null) {
            synchronized (SpringContextUtils.class) {
                if (applicationContext == null) {
                    SpringContextUtils.applicationContext = context;
                }
            }
        }
    }

}
