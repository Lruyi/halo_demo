package com.halo.aop;

import com.halo.annotation.IfApiLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/17 9:52
 */
@Component
@Aspect // 声明该类是一个切面类
public class MyAspect {

//    @Before("execution(* com.example.halo.demo.service.*.*(..))")
    @Before("myPointCut()")
    public void before() {
        System.out.println("前置增强。。。");
    }

//    @AfterReturning("execution(* com.example.halo.demo.service.*.*(..))")
    @AfterReturning("myPointCut()")
    public void afterReturing() {
        System.out.println("后置增强。。。");
    }

//    @Around("execution(* com.example.halo.demo.service.*.*(..))")
//    @Around("myPointCut()")
    @Around("ifApiLogger()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕前增强。。。");

        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        IfApiLogger apiLogger =  objMethod.getDeclaredAnnotation(IfApiLogger.class);
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        String customerName = "";
        if (args.length > apiLogger.customerNameIndex()) {
            customerName = (String) args[apiLogger.customerNameIndex()];
        }
        // TODO

        // 执行原方法
        Object proceed = joinPoint.proceed();// 相当于Object obj = method.invoke();

        // TODO
        System.out.println("环绕后增强。。。");
        return proceed;
    }

//    @AfterThrowing("execution(* com.example.halo.demo.service.*.*(..))")
    @AfterThrowing("myPointCut()")
    public void afterThrowing() {
        System.out.println("异常抛出增强。。。。");
    }

//    @After("execution(* com.example.halo.demo.service.*.*(..))")
    @After("myPointCut()")
    public void after() {
        System.out.println("最终增强。。。");
    }

    @Pointcut("execution(* com.halo.service.*.*(..))")
    private void myPointCut() {}

    @Pointcut("@annotation(com.halo.annotation.IfApiLogger)")
    private void ifApiLogger() {}
}
