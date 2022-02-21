package com.halo.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 流水记录Vo
 * @Author: Halo_ry
 * @Date: 2019/11/25 18:06
 */
@Setter
@Getter
public class FlowingBillListVo implements Serializable {
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

}
