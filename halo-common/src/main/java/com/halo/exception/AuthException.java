package com.halo.exception;


import com.halo.enums.ErrorCodeEnum;

/**
 * @author: wangweichang@tal.com
 * @date: 2025/11/04 11:22
 * @description:
 */
public class AuthException extends RuntimeException {

    private final String errorCode = ErrorCodeEnum.FORBIDDEN.getCode();

    public AuthException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }

}
