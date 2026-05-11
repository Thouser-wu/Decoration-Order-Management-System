package com.gzu.decoration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 材料采购记录实体类
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Data
@TableName("material_purchase")
public class MaterialPurchase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 采购单号（PUR+年月日+序号）
     */
    private String purchaseNo;

    /**
     * 关联材料ID
     */
    private Long materialId;

    /**
     * 采购数量
     */
    private Integer purchaseNum;

    /**
     * 本次采购单价
     */
    private BigDecimal purchasePrice;

    /**
     * 采购总费用（数量×单价）
     */
    private BigDecimal purchaseTotal;

    /**
     * 采购日期
     */
    private LocalDate purchaseTime;

    /**
     * 材料供应商
     */
    private String supplier;

    /**
     * 供应商联系电话
     */
    private String supplierPhone;

    /**
     * 付款方式（现金/微信/支付宝/转账）
     */
    private String paymentMethod;

    /**
     * 付款状态（0未付 1已付）
     */
    private Integer paymentStatus;

    /**
     * 发票号码
     */
    private String invoiceNo;

    /**
     * 备注（品牌、质量等）
     */
    private String remark;

    /**
     * 采购人ID
     */
    private Long createBy;

    /**
     * 记录时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
