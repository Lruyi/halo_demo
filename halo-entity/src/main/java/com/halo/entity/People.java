package com.halo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * people
 * @author Halo_ry
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class People implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    @NotBlank(message="名字不能为空")
//    @Length(max=2, message = "用户名不能超过2个字符")
    private String name;

    private Integer age;

    /**
     * 1-白种人,2-黄种人,3-黑种人,4-混血儿
     */
    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    @TableField(value = "ext_1")
    private String peopleType;

    @TableField(value = "ext_2")
    private Integer grade;

    /**
     * 是否走缓存 默认FALSE
     * 表示该属性不为数据库表字段，但又是必须使用的
     */
    @TableField(exist = false)
    private boolean flag;
}