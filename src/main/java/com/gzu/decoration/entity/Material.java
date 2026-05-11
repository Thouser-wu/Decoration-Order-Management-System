package com.gzu.decoration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 材料信息实体类
 * 
 * @author Thouser-wu
 * @since 2026-05-11
 */
@Data
@TableName("material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 材料名称（水泥、瓷砖、电线、乳胶漆等）
     */
    private String materialName;

    /**
     * 材料分类（水电材料/墙面材料/地面材料/木工材料）
     */
    private String materialType;

    /**
     * 规格型号（如：425号、800*800mm）
     */
    private String specification;

    /**
     * 单位（袋、平方、卷、桶、米）
     */
    private String unit;

    /**
     * 最新采购单价
     */
    private BigDecimal purchasePrice;

    /**
     * 参考售价（用于报价）
     */
    private BigDecimal salePrice;

    /**
     * 当前库存数量
     */
    private Integer stockNum;

    /**
     * 最低库存预警值
     */
    private Integer minStock;

    /**
     * 常用供应商
     */
    private String supplier;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 状态（1启用 0停用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
