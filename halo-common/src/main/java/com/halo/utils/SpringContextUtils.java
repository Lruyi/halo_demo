package com.halo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description:  以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.
 * @author: halo_ry
 * @Date: 2022/6/19 17:06
 */
@Slf4j
@Component
public class SpringContextUtils implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null;

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.debug("注入ApplicationContext到SpringContextHolder:" + applicationContext);

        if (SpringContextUtils.applicationContext != null) {
            log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
                    + SpringContextUtils.applicationContext);
        }
        //NOSONAR
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return getBean(requiredType,true);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param name
     * @param requiredType
     * @param required
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> requiredType,boolean required) {
        assertContextInjected();
        if (required){
            return (T) applicationContext.getBean(name);
        }else{
            String [] beanNames = applicationContext.getBeanNamesForType(requiredType);
            for (String beanName:beanNames){
                if (beanName.equals(name)){
                    return (T) applicationContext.getBean(name);
                }
            }
            return null;
        }
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param requiredType
     * @param required
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType ,boolean required) {
        assertContextInjected();

        if (required){
            return applicationContext.getBean(requiredType);
        }else{
            Map<String, T> typies = applicationContext.getBeansOfType(requiredType);
            T t = null;
            if (typies!=null&&typies.size()>0){
                t = typies.get(requiredType.getSimpleName());
                if (t==null){
                    t = typies.values().iterator().next();
                }
            }
            return t;
        }
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        log.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        SpringContextUtils.clearHolder();
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
    }
}
