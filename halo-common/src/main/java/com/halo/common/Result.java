package com.halo.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 后台返回页面对象
 *
 * @author Halo_ry
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回编码
     */
    private int code = 0;

    /**
     * 返回条数
     */
    private int count = 0;

    /**
     * 返回数据对象
     */
    private T data;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回结果
     */
    private boolean rtnResult = false;

    /**
     * 版本
     */
    private Long version;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isRtnResult() {
        return rtnResult;
    }

    public void setRtnResult(boolean rtnResult) {
        this.rtnResult = rtnResult;
    }

    public Result() {
        super();
    }

    public Result(String msg) {
        super();
        this.msg = msg;
    }

    public Result(int count, String msg) {
        super();
        this.count = count;
        this.msg = msg;
    }

    public Result(T data) {
        super();
        this.data = data;
        if (null != data) {
            this.msg = "操作成功";
            this.rtnResult = true;
        }
    }

    public Result(int code, int count, T data, String msg, boolean rtnResult) {
        super();
        this.code = code;
        this.count = count;
        this.data = data;
        this.msg = msg;
        this.rtnResult = rtnResult;
    }

    public Result(int code, int count, T data, String msg, boolean rtnResult, Long version) {
        super();
        this.code = code;
        this.count = count;
        this.data = data;
        this.msg = msg;
        this.rtnResult = rtnResult;
        this.version = version;
    }

    /**
     * 返回错误对象
     *
     * @return
     * @Title: getFail
     * @Description:
     */
    public static <T> Result<T> getFail() {
        return getFail("操作失败");
    }

    /**
     * 返回错误对象
     *
     * @param msg 消息结果
     * @return
     * @Title: getFail
     * @Description:
     */
    public static <T> Result<T> getFail(String msg) {
        Result<T> result = new Result<T>();
        result.setMsg(msg);
        result.setRtnResult(false);
        return result;
    }

    /**
     *
     * @param code 错误码
     * @param msg 信息结果
     * @param <T>
     * @return
     */
    public static <T> Result<T> getFail(int code, String msg) {
        Result<T> result = new Result<T>();
        result.setMsg(msg);
        result.setCode(code);
        result.setRtnResult(false);
        return result;
    }

    /**
     * 返回成功对象
     *
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess() {
        return getSuccess(0, "查询无数据");
    }


    /**
     * 返回成功对象
     *
     * @param msg 返回消息
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess(String msg) {
        return getSuccess(0, null, msg);
    }


    /**
     * 返回成功对象，不指定count 默认为1条
     *
     * @param data 返回数据
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess(T data) {
        if (data instanceof List) {
            List<?> dataTemp = (List<?>) data;
            if (!CollectionUtils.isEmpty(dataTemp)) {
                return getSuccess(dataTemp.size(), data);
            }
        }
        return getSuccess(1, data);
    }


    /**
     * 返回成功对象
     *
     * @param data 返回数据对象
     * @param msg  返回消息
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess(T data, String msg) {
        return getSuccess(1, data, msg);
    }

    /**
     * 返回成功对象
     *
     * @param data
     * @param msg
     * @param code
     * @return Result<T>
     * @Title: getSuccess
     * @Description:
     * @author hyq
     * @date 2019年9月5日上午11:52:02
     */
    public static <T> Result<T> getSuccess(T data, String msg, int code) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setCount(1);
        result.setData(data);
        result.setMsg(msg);
        result.setVersion(null);
        result.setRtnResult(true);
        return result;
    }


    /**
     * 返回成功对象
     *
     * @param count 返回data条数
     * @param msg   返回消息
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess(int count, String msg) {
        return getSuccess(count, null, msg);
    }


    /**
     * 返回成功对象
     *
     * @param count 返回data条数
     * @param data  返回数据
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess(int count, T data) {
        return getSuccess(count, data, "操作成功");
    }

    /**
     * 返回成功对象
     *
     * @param count 返回data条数
     * @param data  返回数据
     * @param msg   消息结果
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess(int count, T data, String msg) {
        return getSuccess(count, data, msg, null);
    }


    /**
     * 返回成功对象
     *
     * @param count 返回data条数
     * @param data  返回数据
     * @param msg   消息结果
     * @return
     * @Title: getSuccess
     * @Description:
     */
    public static <T> Result<T> getSuccess(int count, T data, String msg, Long version) {
        Result<T> result = new Result<T>();
        result.setCount(count);
        result.setData(data);
        result.setMsg(msg);
        result.setVersion(version);
        result.setRtnResult(true);
        return result;
    }

}
