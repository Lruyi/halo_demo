package com.halo.exception;

import com.halo.constant.ErrorCode;

/**
 * @Description: 业务异常
 * @author: halo_ry
 * @Date: 2022/10/22 17:36
 */
public class BusinessException extends BaseException {


    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(String message) {
        this(ErrorCode.BUSINESS_ERROR, message);
    }

    @Override
    protected boolean isValidCode() {
        return getCode().indexOf(ErrorCode.BUSINESS_ERROR_PREFIX) == 0;
    }
}
