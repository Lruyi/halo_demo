package com.halo.aop;

import com.halo.annotation.ExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(com.halo.annotation.ExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodDesc = getMethodDescription(joinPoint);
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("{}执行完成，耗时：{}ms", methodDesc, executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("{}执行失败，耗时：{}ms，错误：{}", methodDesc, executionTime, e.getMessage());
            throw e;
        }
    }

    private String getMethodDescription(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ExecutionTime annotation = method.getAnnotation(ExecutionTime.class);
        
        String desc = annotation.value();
        if (!StringUtils.hasText(desc)) {
            desc = method.getName();
        }
        
        return desc;
    }
} 