package com.halo.demo.serverresult;

import com.halo.exception.RemoteCallException;
import lombok.Data;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/20 14:11
 */
@Data
public class CommonServerResult<T> extends BaseServerResult<T> {
    private static final long serialVersionUID = -8030578131751566725L;
    private boolean rlt;
    private String code;
    private String message;
    private T data;

    @Override
    public boolean validate(String apiName) {
        if (!getSuccessCode().equals(this.getCode())) {
            throw new RemoteCallException(this.getCode(), "调用【" + apiName + "】接口返回结果异常：" + this.getCode() + "|" + this.getMessage());
        }
        return true;
    }

    /**
     * 远程调用成功响应码
     */
    public String getSuccessCode() {
        return "000000";
    }
}
