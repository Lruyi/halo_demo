package com.halo.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 流水记录Vo
 * @Author: Halo_ry
 * @Date: 2019/11/25 18:06
 */
public class    FlowingBillListVo implements Serializable {
    private static final long serialVersionUID = 7854208336474148931L;

    /**
     * 交易流水号（指资金变动流水号）
     */
    private String sn;

    /**
     * 客户ID
     */
    private Integer custId;

    /**
     * 账户归属：0:采购，1：供应，2平台，3保险，4酒店
     */
    private Integer merchentType;

    /**
     * 外部客户ID
     */
    private Integer otherCustId;

    /**
     * 业务关联单号（外部客户ID或者相关交易流水号）
     */
    private String businessAssociationSn;

    /**
     * 关联原单号
     */
    private String accociationOriginalSn;

    /**
     * 业务类型
     */
    private String buinessType;

    /**
     * 交易类型
     */
    private String transactionType;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 三方流水号
     */
    private String tradeNo;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 商户名称
     */
    private String customerName;

    /**
     * 对于出票来说这里存入供应商名称
     */
    private String supplierName;

    /**
     * 供应商运营类型
     */
    private Integer typeOperation;

    /**
     * 出票officeNo
     */
    private String ticketOfficeNo;

    /**
     * 账户类型
     */
    private String subAccountType;

    /**
     * 帐务变动方向，1来账，2往账
     */
    private String seqFlag;

    /**
     * 交易金额
     */
    private BigDecimal transactionAmount;

    /**
     * 可用额度（授信账户）
     */
    private BigDecimal availableCredit;

    /**
     * 总额度（授信账户）
     */
    private BigDecimal totalDegree;

    /**
     * 总余额（储蓄账户）
     */
    private BigDecimal totalAmount;

    /**
     * 可用额度（储蓄账户）
     */
    private BigDecimal availabilityAmount;

    /**
     * 冻结金额（储蓄账户）
     */
    private BigDecimal freezeAmount;

    /**
     * 交易人
     */
    private String operator;

    /**
     * 交易时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 国内国际: 国内--domestic  国际--international
     */
    private String domesticInternational;

    public String getDomesticInternational() {
        return domesticInternational;
    }

    public void setDomesticInternational(String domesticInternational) {
        this.domesticInternational = domesticInternational;
    }

    public String getAccociationOriginalSn() {
        return accociationOriginalSn;
    }

    public void setAccociationOriginalSn(String accociationOriginalSn) {
        this.accociationOriginalSn = accociationOriginalSn;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public BigDecimal getTotalDegree() {
        return totalDegree;
    }

    public void setTotalDegree(BigDecimal totalDegree) {
        this.totalDegree = totalDegree;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getMerchentType() {
        return merchentType;
    }

    public void setMerchentType(Integer merchentType) {
        this.merchentType = merchentType;
    }

    public Integer getOtherCustId() {
        return otherCustId;
    }

    public void setOtherCustId(Integer otherCustId) {
        this.otherCustId = otherCustId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBusinessAssociationSn() {
        return businessAssociationSn;
    }

    public void setBusinessAssociationSn(String businessAssociationSn) {
        this.businessAssociationSn = businessAssociationSn;
    }

    public String getBuinessType() {
        return buinessType;
    }

    public void setBuinessType(String buinessType) {
        this.buinessType = buinessType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(Integer typeOperation) {
        this.typeOperation = typeOperation;
    }

    public String getTicketOfficeNo() {
        return ticketOfficeNo;
    }

    public void setTicketOfficeNo(String ticketOfficeNo) {
        this.ticketOfficeNo = ticketOfficeNo;
    }

    public String getSubAccountType() {
        return subAccountType;
    }

    public void setSubAccountType(String subAccountType) {
        this.subAccountType = subAccountType;
    }

    public String getSeqFlag() {
        return seqFlag;
    }

    public void setSeqFlag(String seqFlag) {
        this.seqFlag = seqFlag;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAvailabilityAmount() {
        return availabilityAmount;
    }

    public void setAvailabilityAmount(BigDecimal availabilityAmount) {
        this.availabilityAmount = availabilityAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
}
