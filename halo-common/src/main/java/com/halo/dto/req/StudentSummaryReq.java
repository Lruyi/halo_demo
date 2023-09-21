package com.halo.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/20 14:25
 */
@Data
public class StudentSummaryReq {
    /**
     * 学员uid
     */
    @NotNull(message = "学员uid不能为空")
    private Integer studentUid;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    @Length(min = 8, max = 8, message = "参数startDate的格式为：yyyyMMdd")
    private String startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空")
    @Length(min = 8, max = 8, message = "参数endDate的格式为：yyyyMMdd")
    private String endDate;

    /**
     * 查询类型【0：所有（含有报名、试听、回放），1：报名：2试听】
     */
    private Integer queryType = 0;
}
