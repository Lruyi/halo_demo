package com.halo.exception;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/26 15:16
 */
public class RemoteCallException extends RuntimeException {

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
