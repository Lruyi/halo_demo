package com.halo.req;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/5/29 16:28
 */
@Data
@Builder
public class ParseTheWeekReq implements Serializable {
    private static final long serialVersionUID = -6582285246232244340L;

    /**
     * 排课类型 1-前置排课  2-后置排课
     */
    private Integer arrangeType;

    /**
     * 课次号list
     */
    private List<Integer> curriculumNos;

    /**
     *     1-每n日解锁1课次  2-每周x解锁1课次（可多选)
     * 示例：
     *    {"arrangeRuleType" : 1, "arrangeRuleContent": ["1"]}
     *    {"arrangeRuleType" : 2, "arrangeRuleContent": ["1","2","5"]}
     */
    private Integer arrangeRuleType;

    /**
     * ["1","2","5"]
     */
    private List<String> arrangeRuleContent;

    /**
     * 缴费时间(yyyy-MM-dd)
     */
    private String payDate;
}
