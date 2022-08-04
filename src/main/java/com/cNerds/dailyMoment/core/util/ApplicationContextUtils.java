package com.cNerds.dailyMoment.core.util;

import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class ApplicationContextUtils {

    public static <T> T getBeanByType(ApplicationContext applicationContext, Class<T> clazz) {
        Map<String, T> beanMap = applicationContext.getBeansOfType(clazz);
        int size = beanMap.size();
        if (size == 0) {
            if (applicationContext.getParent() != null)
                return getBeanByType(applicationContext.getParent(), clazz);
            throw new NoSuchBeanDefinitionException(clazz.getSimpleName());
        }
        if (size > 1)
            throw new NoSuchBeanDefinitionException("No unique bean definition type [" + clazz.getSimpleName() + "]");
        return (T) beanMap.values().iterator().next();
    }
}