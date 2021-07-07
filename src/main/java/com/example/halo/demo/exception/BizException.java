package com.example.halo.demo.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/17 23:44
 */
@Getter
@Setter
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 8879627714434406716L;
    /**
     * 异常信息
     */
    private String msg;

    /**
     * 异常码
     */
    private int code;

    public BizException(){
        super();
    }

    public BizException(String msg) {
        this.msg = msg;
    }

    public BizException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public BizException(String message, int code, Object... args) {
        super(String.format(message, args));
        this.msg = String.format(message, args);
        this.code = code;
    }

    public BizException(String message, Throwable cause, String msg, int code) {
        super(message, cause);
        this.msg = msg;
        this.code = code;
    }

    public BizException(Throwable cause, String msg, int code) {
        super(cause);
        this.msg = msg;
        this.code = code;
    }
}
