package com.halo.exception;

/**
 * 远程调用异常
 *
 * @author zhy
 */
public class RemoteCallException extends RuntimeException {
    /**
     * 三方返回的响应码
     */
    private String code;

    public RemoteCallException(String code, String message) {
        super(message);
        this.code = code;
    }

    public RemoteCallException(String message) {
        this("0", message);
    }

    public String getCode() {
        return code;
    }
}
