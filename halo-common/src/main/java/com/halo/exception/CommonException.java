package com.halo.exception;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/10/22 17:40
 */
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = -2529514596825329256L;
    private String code;

    public CommonException(String code) {
        super();
        this.code=code;
    }

    public CommonException(String code,String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
        this.code=code;
    }

    public CommonException(String code,String arg0, Throwable arg1) {
        super(arg0, arg1);
        this.code=code;
    }

    public CommonException(String code,String arg0) {
        super(arg0);
        this.code=code;
    }

    public CommonException(String code,Throwable arg0) {
        super(arg0);
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
