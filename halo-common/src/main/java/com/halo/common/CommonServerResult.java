package com.halo.common;

import lombok.Data;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/20 14:11
 */
@Data
public class CommonServerResult<T> {
    private boolean rlt;
    private String code;
    private String message;
    private T data;
}
