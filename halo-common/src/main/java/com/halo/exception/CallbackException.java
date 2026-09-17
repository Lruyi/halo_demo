package com.halo.exception;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description: 回调异常，用于触发 @Retryable 重试
 */
public class CallbackException extends RuntimeException {

    public CallbackException(String message) {
        super(message);
    }

    public CallbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
