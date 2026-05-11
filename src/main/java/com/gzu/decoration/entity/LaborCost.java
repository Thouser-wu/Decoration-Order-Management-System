package com.gzu.decoration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 人工成本实体类
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Data
@TableName("labor_cost")
public class LaborCost implements Serializable {

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
     * 人工类型（水电工/泥瓦工/木工/油漆工/杂工）
     */
    private String laborType;

    /**
     * 工人姓名
     */
    private String workerName;

    /**
     * 用工天数（支持半天0.5）
     */
    private BigDecimal workDays;

    /**
     * 单日工价
     */
    private BigDecimal dayPrice;

    /**
     * 人工总费用（天数×日价）
     */
    private BigDecimal laborTotal;

    /**
     * 工作日期
     */
    private LocalDate workDate;

    /**
     * 付款状态（0未付 1已付）
     */
    private Integer paymentStatus;

    /**
     * 备注（工作内容、加班情况等）
     */
    private String remark;

    /**
     * 记录时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
