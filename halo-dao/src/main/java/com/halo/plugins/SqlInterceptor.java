package com.halo.plugins;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/3/16 11:03
 */

/**
 * 1.Executor：拦截执行器的方法。
 * 2.ParameterHandler：拦截参数的处理。
 * 3.ResultHandler：拦截结果集的处理。
 * 4.StatementHandler：拦截Sql语法构建的处理。
 */
@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SqlInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        Object obj = boundSql.getParameterObject();
        String sql = boundSql.getSql();
        log.info("sql: {}", sql);
        return invocation.proceed();
    }
}
