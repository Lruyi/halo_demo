package com.halo.demo.serverresult;

import java.io.Serializable;

public abstract class BaseServerResult<T> implements Serializable {
    /**
     * 校验响应结果是否正常
     * @param message
     * @return
     */
    public abstract boolean validate(String message);

    public abstract T getData();
}
