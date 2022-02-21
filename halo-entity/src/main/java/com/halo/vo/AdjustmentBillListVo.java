package com.halo.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
*  调额记录vo
* @author Halo_ry
*/
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

	public BigDecimal getAvailabilityAmountBefore() {
		return availabilityAmountBefore;
	}

	public void setAvailabilityAmountBefore(BigDecimal availabilityAmountBefore) {
		this.availabilityAmountBefore = availabilityAmountBefore;
	}

	public BigDecimal getAvailabilityAmountAfter() {
		return availabilityAmountAfter;
	}

	public void setAvailabilityAmountAfter(BigDecimal availabilityAmountAfter) {
		this.availabilityAmountAfter = availabilityAmountAfter;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getMerchentType() {
		return merchentType;
	}

	public void setMerchentType(Integer merchentType) {
		this.merchentType = merchentType;
	}

	public BigDecimal getPreamount() {
		return preamount;
	}

	public void setPreamount(BigDecimal preamount) {
		this.preamount = preamount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}