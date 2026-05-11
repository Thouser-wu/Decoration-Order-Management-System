package com.gzu.decoration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收款记录实体类
 */
@Data
@TableName("payment_record")
public class PaymentRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;
    private String paymentNo;
    private String paymentType;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private String voucherImage;
    private String remark;
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
