package com.halo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * tb_subaccount
 * @author Halo_ry
 */
@Getter
@Setter
@ToString
public class Subaccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer subaccountId;

    /**
     * 账户编号，唯一标识
     */
    private String custAccount;

    /**
     * 客户编号
     */
    private Integer custId;

    /**
     * 账户金额
     */
    private BigDecimal amount;

    /**
     * 冻结金额
     */
    private BigDecimal freezeAmount;

    /**
     * 已用金额
     */
    private BigDecimal usedAmount;

    /**
     * 可用金额=账户金额-冻结金额-已用金额
     */
    private BigDecimal availabilityAmount;

    /**
     * 账户号类型:01 储蓄账户，02 授信账户
     */
    private String subaccountType;

    /**
     * 账户状态：01生效，02冻结，03注销
     */
    private String state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 计息积数
     */
    private BigDecimal interestBase;

    /**
     * 上期计息的时间
     */
    private Date lasttermInterestTime;

    /**
     * 最新修改时间
     */
    private Date lastupdateTime;

    /**
     * 上期总金额,计息使用
     */
    private BigDecimal lasttermAmount;

    /**
     * 校验码,MD5加密，防篡改
     */
    private String checkValue;

    /**
     * 版本号，悲观锁
     */
    private Integer version;

    /**
     * 交易密码
     */
    private String tradePwd;

    /**
     * 支付时候是否需要输入密码0 否，1是
     */
    private String checkTradePwd;

    /**
     * 父ID
     */
    private Integer parentId;
}