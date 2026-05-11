package com.gzu.decoration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单材料消耗实体类
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Data
@TableName("order_material")
public class OrderMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 关联材料ID
     */
    private Long materialId;

    /**
     * 消耗数量
     */
    private BigDecimal useNum;

    /**
     * 材料单价（采购价）
     */
    private BigDecimal unitPrice;

    /**
     * 该材料消耗成本（数量×单价）
     */
    private BigDecimal useCost;

    /**
     * 使用日期
     */
    private LocalDate useDate;

    /**
     * 备注（使用位置、浪费情况等）
     */
    private String remark;

    /**
     * 记录时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
