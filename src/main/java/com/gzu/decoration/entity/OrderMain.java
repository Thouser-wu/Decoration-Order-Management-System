package com.gzu.decoration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Data
@TableName("order_main")
public class OrderMain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单单号（ORD+年月日+序号）
     */
    private String orderNo;

    /**
     * 关联客户ID
     */
    private Long customerId;

    /**
     * 装修类型（家装/工装/局部翻新）
     */
    private String projectType;

    /**
     * 装修项目内容详细描述
     */
    private String projectContent;

    /**
     * 订单状态（0待施工 1施工中 2已完工 3已结算 4已取消）
     */
    private Integer orderStatus;

    /**
     * 报价总金额
     */
    private BigDecimal quoteTotal;

    /**
     * 材料总成本
     */
    private BigDecimal costMaterial;

    /**
     * 人工总成本
     */
    private BigDecimal costLabor;

    /**
     * 总成本（材料+人工）
     */
    private BigDecimal costTotal;

    /**
     * 本单利润
     */
    private BigDecimal profitTotal;

    /**
     * 利润率（%）
     */
    private BigDecimal profitRate;

    /**
     * 开工时间
     */
    private LocalDate startTime;

    /**
     * 预计完工时间
     */
    private LocalDate endTime;

    /**
     * 实际完工时间
     */
    private LocalDate actualEndTime;

    /**
     * 收款状态（0未收款 1部分收款 2已全款）
     */
    private Integer paymentStatus;

    /**
     * 已收金额
     */
    private BigDecimal receivedAmount;

    /**
     * 欠款金额
     */
    private BigDecimal arrearsAmount;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 接单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
