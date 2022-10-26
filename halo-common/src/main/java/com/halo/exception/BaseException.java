package com.halo.exception;

import com.halo.constant.ErrorCode;

/**
 * @Description: 基础异常类
 * @author: halo_ry
 * @Date: 2022/10/22 17:37
 */
public class BaseException extends CommonException {

    public BaseException(String code, String message, Throwable cause) {
        super(code, message, cause);
        if(!isValidCode()){
            throw new RuntimeException("Invalid error code!");
        }
    }

    public BaseException(String code, String message) {
        super(code, message);
        if(!isValidCode()){
            throw new RuntimeException("Invalid error code!");
        }
    }

    public BaseException(ErrorCode errorCode, String message, Throwable cause) {
        this(errorCode.getCode(), message, cause);
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        this(errorCode.getCode(), errorCode.getDesc(), cause);
    }

    public BaseException(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), message);
    }

    public BaseException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getDesc());
    }

    protected boolean isValidCode(){
        return true;
    }
}
