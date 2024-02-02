package com.halo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserInfo {

    /**
     * 学员uid
     */
    private String id;

    /**
     * 学员编号
     */
    private String card;

    /**
     * 学员id
     */
    private String studentId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 是否新老学员, 0:未缴纳过报名费,新学员 1:缴纳过报名费
     */
    private Integer isPaidRegfee;

    private String phone;

    private String bindingCode;

    private Integer userType;

    private String birthday;

    private String sex;

    private String gtid;

    private String grid;

    private Integer classCount;

    private String mphone;

    private Integer fsite;

    private String areaCode;

    private Integer isfreeze;

    private Date modified;

    private Integer isComplete;

    /**
     * 学员身份 1-新生 2-老生
     */
    private Integer studentTag;
}
