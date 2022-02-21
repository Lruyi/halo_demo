package com.halo.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
*  调额记录vo
* @author Halo_ry
*/
@Getter
@Setter
public class AdjustmentBillListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 流水号
    */
    private String sn;

    /**
    * 客户编号(冗余)
    */
    private Integer custId;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 账户归属
	 */
	private Integer merchentType;

    /**
    * 变动前总金额
    */
    private BigDecimal preamount;

	/**
	 * 资金变动前可用总金额
	 */
	private BigDecimal availabilityAmountBefore;

    /**
    * 变动后总金额
    */
    private BigDecimal amount;

	/**
	 * 资金变动后可用总金额
	 */
	private BigDecimal availabilityAmountAfter;

    /**
    * 创建日期
    */
    private Date createTime;

    /**
    * 备注
    */
    private String remark;

	/**
	 * 操作人
	 */
	private String operator;
}