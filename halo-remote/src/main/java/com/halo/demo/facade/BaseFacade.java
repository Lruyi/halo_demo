package com.halo.demo.facade;

import cn.hutool.json.JSONUtil;
import com.halo.demo.serverresult.BaseServerResult;
import com.halo.exception.RemoteCallException;
import org.slf4j.Logger;

/**
 * 基础门面
 *
 * @author zhy
 */
public abstract class BaseFacade {
    /**
     * 统一处理调用Feign客户端出现的异常
     *
     * @param e
     * @param apiName
     * @param requestParams
     * @param log
     */
    protected void dealException(String apiName, Logger log, Exception e, Object requestParams) {
        log.error("调用【" + apiName + "】接口出现异常，请求参数：" + (requestParams instanceof String ? requestParams :
                JSONUtil.toJsonStr(requestParams)), e);
        throw new RemoteCallException("调用【" + apiName + "】接口出现异常：" + e.getMessage());
    }

    /**
     * 输出请求结果
     *
     * @param apiName
     * @param request
     * @param response
     * @param log
     */
    protected void printResult(String apiName, Logger log, Object request, Object response) {
        if (log.isInfoEnabled()) {
            log.info("调用【{}】接口，请求参数：{}，响应结果：{}", apiName,
                    request == null ? "" : (request instanceof String ? request : JSONUtil.toJsonStr(request)),
                    response == null ? "" : (response instanceof String ? response : JSONUtil.toJsonStr(response)));
        }
    }

    /**
     * 输出错误响应结果
     *
     * @param apiName
     * @param log
     * @param request
     * @param response
     */
    protected void printErrorResult(String apiName, Logger log, Object request, Object response) {
        log.error("调用【{}】接口响应结果异常，请求参数：{}，响应结果：{}", apiName,
                request == null ? "" : (request instanceof String ? request : JSONUtil.toJsonStr(request)),
                response == null ? "" : (response instanceof String ? response : JSONUtil.toJsonStr(response)));
    }

    /**
     * 获取远程调用数据
     *
     * @param apiName
     * @param request
     * @param log
     * @param remoteCall
     * @param <T>
     * @return
     */
    protected <T> T getData(String apiName, Object request, Logger log, RemoteCall remoteCall) {
        BaseServerResult<T> serverResult = null;
        try {
            serverResult = remoteCall.call();
            serverResult.validate(apiName);
        } catch (RemoteCallException e) {
            printErrorResult(apiName, log, request, serverResult);
            throw e;
        } catch (Exception e) {
            dealException(apiName, log, e, request);
        }
        printResult(apiName, log, request, serverResult);
        return serverResult.getData();
    }
}
