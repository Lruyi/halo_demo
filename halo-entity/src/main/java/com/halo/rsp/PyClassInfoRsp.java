package com.halo.rsp;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/5/25 16:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PyClassInfoRsp implements Serializable {

    private String tagTransName;

    private String gradeId;

    private String tagId;

    private String cityId;

    private String tagName;

    private Integer courseMaxPersons;


    private String id;

    private Integer classType;

    private Integer productType;


    /**
     * 班级id
     */
    private String classId;
    /**
     * 课程类型（盒子课、精品课、微课等）
     */
    private Integer courseType;
    /**
     * 课程用途标签（int， 示例：3）
     */
    private Integer useLabel;

    /**
     * 授课类型
     */
    private Integer categoryType;

    /**
     * 排课类型 1-前置排课  2-后置排课
     */
    private Integer arrangeType;

    /**
     * 解锁规则 1-每n日解锁1课次  2-每周x解锁1课次（可多选)
     */
    private ArrangeRule arrangeRule;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ArrangeRule {

        /**
         * 1-每n日解锁1课次  2-每周x解锁1课次（可多选)
         */
        private Integer arrangeRuleType;

        /**
         *
         */
        private List<String> arrangeRuleContent;
    }
}
