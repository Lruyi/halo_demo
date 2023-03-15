package com.halo.req;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户资金变动流水表
 *
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@TableName("tb_subaccount_seq")
public class SubaccountSeq implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 流水号
     */
    private String sn;

    /**
     * 子账户id
     */
    private Long subaccountId;

    /**
     * 客户编号(冗余)
     */
    private Long custId;

    /**
     * 账户号类型(冗余)
     */
    private String subaccountType;


    /**
     * 业务类型
     */
    private String buinessType;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 用户账户
     */
    private String custAccount;

    /**
     * 帐务变动方向，1来账，2往账
     */
    private Integer seqFlag;

    /**
     * 类型：01充值 ，02支付 ，03提现，04 内部调账 ，05 结息，06原交易退款，07原交易撤销
     */
    private String changeType;

    /**
     * 资金变动金额
     */
    private BigDecimal changeAmount;

    /**
     * 变动前总金额
     */
    private BigDecimal preAmount;


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
     * 关联流水id:对应不同的表，充值流水，支付流水，提现流水
     */
    private String refSn;

    /**
     * 对账状态
     */
    private String checkState;

    /**
     * 订单号（冗余）
     */
    private String orderId;

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