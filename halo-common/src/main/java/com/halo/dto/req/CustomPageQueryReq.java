package com.halo.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 课表查询通用接口入参
 * @author: halo_ry
 * @Date: 2023/9/26 10:03
 */
@Data
public class CustomPageQueryReq {

    /**
     * 查询类型：<p/>
     * Summary-聚合课表 <p/>
     * StudentClass-学员上课班级 <p/>
     * StudentInStudyClass-学员在读班级 <p/>
     */
    @NotBlank(message = "查询类型不能为空")
    private String queryType;

    /**
     * 查询字段列表
     */
    @NotEmpty(message = "查询字段列表不能为空")
    private List<String> fieldList;

    /**
     * 查询条件列表
     */
    @NotEmpty(message = "查询条件列表不能为空")
    private List<Condition> conditionList;

    /**
     * 排序字段列表
     */
    private List<OrderBy> orderByList;

    /**
     * 是否去重
     */
    private boolean distinct = false;

    /**
     * 是否查总条数
     */
    private boolean searchCount;

    private long pageNo = 1;
    private long pageSize = 100;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Condition {

        /**
         * 字段名称
         */
        private String field;

        /**
         * 操作符: IN, =, <>, >, >=, <, <=, IS NULL, IS NOT NULL, BETWEEN
         */
        private String operator;

        /**
         * 字段值
         */
        private Object value;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class OrderBy {

        /**
         * 排序字段名称
         */
        private String field;

        /**
         * 是否为倒序
         */
        private boolean desc;
    }

}
