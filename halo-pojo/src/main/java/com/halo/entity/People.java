package com.halo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}