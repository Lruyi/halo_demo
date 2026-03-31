package com.halo.demo.facade;


import com.halo.demo.serverresult.BaseServerResult;

/**
 * 远程调用执行接口
 *
 * @author zhy
 */
@FunctionalInterface
public interface RemoteCall {
    /**
     * 远程调用执行
     *
     * @return
     */
    BaseServerResult call();
}
