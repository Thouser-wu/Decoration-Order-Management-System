package com.gzu.decoration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户信息实体类
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Data
@TableName("customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 装修地址
     */
    private String address;

    /**
     * 房屋类型（新房/二手房/商铺）
     */
    private String houseType;

    /**
     * 房屋面积（㎡）
     */
    private BigDecimal area;

    /**
     * 客户备注（装修需求、偏好等）
     */
    private String remark;

    /**
     * 客户来源（转介绍/网络平台/线下）
     */
    private String source;

    /**
     * 登记时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
