-- ============================================
-- 装修工人接单管理系统 - 数据库建表脚本
-- 数据库版本: MySQL 8.0+
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_unicode_ci
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `decoration_order_db` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `decoration_order_db`;

-- ============================================
-- 1. 用户表 (sys_user)
-- 存储装修工人登录账号信息
-- ============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '登录账号',
  `password` VARCHAR(100) NOT NULL COMMENT '登录密码（BCrypt加密）',
  `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `role` VARCHAR(20) DEFAULT 'worker' COMMENT '角色（admin管理员/worker普通工人）',
  `status` TINYINT DEFAULT 1 COMMENT '账号状态（1正常 0禁用）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 客户信息表 (customer)
-- 存储装修客户基础信息
-- ============================================
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_name` VARCHAR(50) NOT NULL COMMENT '客户姓名',
  `customer_phone` VARCHAR(20) NOT NULL COMMENT '客户电话',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '装修地址',
  `house_type` VARCHAR(50) DEFAULT NULL COMMENT '房屋类型（新房/二手房/商铺）',
  `area` DECIMAL(10,2) DEFAULT NULL COMMENT '房屋面积（㎡）',
  `remark` TEXT COMMENT '客户备注（装修需求、偏好等）',
  `source` VARCHAR(50) DEFAULT NULL COMMENT '客户来源（转介绍/网络平台/线下）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登记时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_customer_phone` (`customer_phone`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户信息表';

-- ============================================
-- 3. 订单主表 (order_main)
-- 核心订单表，每接一个装修单生成一条记录
-- ============================================
DROP TABLE IF EXISTS `order_main`;
CREATE TABLE `order_main` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单单号（ORD+年月日+序号）',
  `customer_id` BIGINT NOT NULL COMMENT '关联客户ID',
  `project_type` VARCHAR(50) NOT NULL COMMENT '装修类型（家装/工装/局部翻新）',
  `project_content` TEXT NOT NULL COMMENT '装修项目内容详细描述',
  `order_status` TINYINT DEFAULT 0 COMMENT '订单状态（0待施工 1施工中 2已完工 3已结算 4已取消）',
  `quote_total` DECIMAL(12,2) DEFAULT 0.00 COMMENT '报价总金额',
  `cost_material` DECIMAL(12,2) DEFAULT 0.00 COMMENT '材料总成本',
  `cost_labor` DECIMAL(12,2) DEFAULT 0.00 COMMENT '人工总成本',
  `cost_total` DECIMAL(12,2) DEFAULT 0.00 COMMENT '总成本（材料+人工）',
  `profit_total` DECIMAL(12,2) DEFAULT 0.00 COMMENT '本单利润',
  `profit_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '利润率（%）',
  `start_time` DATE DEFAULT NULL COMMENT '开工时间',
  `end_time` DATE DEFAULT NULL COMMENT '预计完工时间',
  `actual_end_time` DATE DEFAULT NULL COMMENT '实际完工时间',
  `payment_status` TINYINT DEFAULT 0 COMMENT '收款状态（0未收款 1部分收款 2已全款）',
  `received_amount` DECIMAL(12,2) DEFAULT 0.00 COMMENT '已收金额',
  `arrears_amount` DECIMAL(12,2) DEFAULT 0.00 COMMENT '欠款金额',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '接单时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_order_status` (`order_status`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表';

-- ============================================
-- 4. 材料信息表 (material)
-- 装修常用材料库，维护材料基础信息和实时库存
-- ============================================
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `material_name` VARCHAR(100) NOT NULL COMMENT '材料名称（水泥、瓷砖、电线、乳胶漆等）',
  `material_type` VARCHAR(50) NOT NULL COMMENT '材料分类（水电材料/墙面材料/地面材料/木工材料）',
  `specification` VARCHAR(100) DEFAULT NULL COMMENT '规格型号（如：425号、800*800mm）',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位（袋、平方、卷、桶、米）',
  `purchase_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '最新采购单价',
  `sale_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '参考售价（用于报价）',
  `stock_num` INT DEFAULT 0 COMMENT '当前库存数量',
  `min_stock` INT DEFAULT 10 COMMENT '最低库存预警值',
  `supplier` VARCHAR(100) DEFAULT NULL COMMENT '常用供应商',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注说明',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1启用 0停用）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_material_type` (`material_type`) USING BTREE,
  KEY `idx_material_name` (`material_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='材料信息表';

-- ============================================
-- 5. 材料采购记录表 (material_purchase)
-- 每次购买材料的详细记录，用于成本追溯和供应商对账
-- ============================================
DROP TABLE IF EXISTS `material_purchase`;
CREATE TABLE `material_purchase` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `purchase_no` VARCHAR(50) NOT NULL COMMENT '采购单号（PUR+年月日+序号）',
  `material_id` BIGINT NOT NULL COMMENT '关联材料ID',
  `purchase_num` INT NOT NULL COMMENT '采购数量',
  `purchase_price` DECIMAL(10,2) NOT NULL COMMENT '本次采购单价',
  `purchase_total` DECIMAL(12,2) NOT NULL COMMENT '采购总费用（数量×单价）',
  `purchase_time` DATE NOT NULL COMMENT '采购日期',
  `supplier` VARCHAR(100) NOT NULL COMMENT '材料供应商',
  `supplier_phone` VARCHAR(20) DEFAULT NULL COMMENT '供应商联系电话',
  `payment_method` VARCHAR(50) DEFAULT NULL COMMENT '付款方式（现金/微信/支付宝/转账）',
  `payment_status` TINYINT DEFAULT 0 COMMENT '付款状态（0未付 1已付）',
  `invoice_no` VARCHAR(50) DEFAULT NULL COMMENT '发票号码',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注（品牌、质量等）',
  `create_by` BIGINT DEFAULT NULL COMMENT '采购人ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_purchase_no` (`purchase_no`) USING BTREE,
  KEY `idx_material_id` (`material_id`) USING BTREE,
  KEY `idx_purchase_time` (`purchase_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='材料采购记录表';

-- ============================================
-- 6. 订单材料消耗表 (order_material)
-- 每个订单实际消耗的材料明细，用于精确核算材料成本
-- ============================================
DROP TABLE IF EXISTS `order_material`;
CREATE TABLE `order_material` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
  `material_id` BIGINT NOT NULL COMMENT '关联材料ID',
  `use_num` DECIMAL(10,2) NOT NULL COMMENT '消耗数量',
  `unit_price` DECIMAL(10,2) NOT NULL COMMENT '材料单价（采购价）',
  `use_cost` DECIMAL(12,2) NOT NULL COMMENT '该材料消耗成本（数量×单价）',
  `use_date` DATE DEFAULT NULL COMMENT '使用日期',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注（使用位置、浪费情况等）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_material_id` (`material_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单材料消耗表';

-- ============================================
-- 7. 人工成本表 (labor_cost)
-- 装修人工工时/工费成本记录，支持多工种、多次录入
-- ============================================
DROP TABLE IF EXISTS `labor_cost`;
CREATE TABLE `labor_cost` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
  `labor_type` VARCHAR(50) NOT NULL COMMENT '人工类型（水电工/泥瓦工/木工/油漆工/杂工）',
  `worker_name` VARCHAR(50) DEFAULT NULL COMMENT '工人姓名',
  `work_days` DECIMAL(5,2) NOT NULL COMMENT '用工天数（支持半天0.5）',
  `day_price` DECIMAL(10,2) NOT NULL COMMENT '单日工价',
  `labor_total` DECIMAL(12,2) NOT NULL COMMENT '人工总费用（天数×日价）',
  `work_date` DATE DEFAULT NULL COMMENT '工作日期',
  `payment_status` TINYINT DEFAULT 0 COMMENT '付款状态（0未付 1已付）',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注（工作内容、加班情况等）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_labor_type` (`labor_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人工成本表';

-- ============================================
-- 8. 报价模板表 (quote_template)
-- 预设常用装修工序报价模板
-- ============================================
DROP TABLE IF EXISTS `quote_template`;
CREATE TABLE `quote_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称（如：水电改造、墙面刷漆）',
  `project_type` VARCHAR(50) DEFAULT NULL COMMENT '适用装修类型',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位（平方/米/项）',
  `reference_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '参考单价',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '工艺说明',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1启用 0停用）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_project_type` (`project_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报价模板表';

-- ============================================
-- 9. 报价明细表 (quote_item)
-- 订单报价的详细工序列表
-- ============================================
DROP TABLE IF EXISTS `quote_item`;
CREATE TABLE `quote_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
  `item_name` VARCHAR(100) NOT NULL COMMENT '工序名称',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位',
  `quantity` DECIMAL(10,2) NOT NULL COMMENT '工程量',
  `unit_price` DECIMAL(10,2) NOT NULL COMMENT '单价',
  `total_price` DECIMAL(12,2) NOT NULL COMMENT '单项金额（单价×工程量）',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注说明',
  `sort_order` INT DEFAULT 0 COMMENT '排序号',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报价明细表';

-- ============================================
-- 10. 收款记录表 (payment_record)
-- 订单收款记录，支持分多次收款
-- ============================================
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
  `payment_no` VARCHAR(50) NOT NULL COMMENT '收款单号（PAY+年月日+序号）',
  `payment_type` VARCHAR(50) NOT NULL COMMENT '收款类型（定金/进度款/尾款）',
  `payment_amount` DECIMAL(12,2) NOT NULL COMMENT '收款金额',
  `payment_method` VARCHAR(50) DEFAULT NULL COMMENT '付款方式（现金/微信/支付宝/银行转账）',
  `payment_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收款时间',
  `voucher_image` VARCHAR(500) DEFAULT NULL COMMENT '收款凭证图片路径',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注说明',
  `create_by` BIGINT DEFAULT NULL COMMENT '收款人ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_payment_time` (`payment_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收款记录表';

-- ============================================
-- 11. 系统配置表 (system_config)
-- 系统配置信息
-- ============================================
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` VARCHAR(500) DEFAULT NULL COMMENT '配置值',
  `config_desc` VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ============================================
-- 12. 操作日志表 (operation_log)
-- 记录用户关键操作日志
-- ============================================
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '操作用户名',
  `operation` VARCHAR(200) NOT NULL COMMENT '操作描述',
  `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
  `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
  `response_time` INT DEFAULT NULL COMMENT '响应时间（毫秒）',
  `status` TINYINT DEFAULT 1 COMMENT '操作状态（1成功 0失败）',
  `error_msg` TEXT COMMENT '错误信息',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认管理员账号（密码：admin123，BCrypt加密后的值）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `status`) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800138000', 'admin', 1);

-- 插入系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `config_desc`) VALUES
('company_name', 'XX装修服务部', '公司名称'),
('company_phone', '400-888-8888', '公司电话'),
('company_address', 'XX市XX区XX路XX号', '公司地址'),
('quote_valid_days', '30', '报价单有效期（天）'),
('min_profit_rate', '10', '最低利润率预警值（%）');

-- 插入常用材料数据
INSERT INTO `material` (`material_name`, `material_type`, `specification`, `unit`, `purchase_price`, `sale_price`, `stock_num`, `min_stock`, `supplier`) VALUES
('水泥', '水电材料', '425号', '袋', 25.00, 30.00, 100, 20, 'XX建材市场'),
('沙子', '水电材料', '中沙', '立方', 120.00, 150.00, 50, 10, 'XX建材市场'),
('电线', '水电材料', '2.5平方', '卷', 150.00, 180.00, 30, 10, 'XX电线厂'),
('水管', '水电材料', 'PPR 20mm', '米', 8.00, 12.00, 200, 50, 'XX管业'),
('乳胶漆', '墙面材料', '立邦净味', '桶', 280.00, 350.00, 20, 5, 'XX涂料店'),
('腻子粉', '墙面材料', '耐水型', '袋', 35.00, 45.00, 80, 20, 'XX建材市场'),
('瓷砖', '地面材料', '800*800mm', '平方', 80.00, 120.00, 100, 20, 'XX瓷砖店'),
('木地板', '地面材料', '复合地板', '平方', 120.00, 180.00, 50, 10, 'XX地板店'),
('石膏板', '木工材料', '12mm', '张', 45.00, 60.00, 40, 10, 'XX建材市场'),
('木龙骨', '木工材料', '3*4cm', '根', 8.00, 12.00, 100, 30, 'XX木材店');

-- 插入报价模板数据
INSERT INTO `quote_template` (`template_name`, `project_type`, `unit`, `reference_price`, `description`) VALUES
('水电改造', '家装', '平方', 120.00, '包含开槽、布管、穿线、封槽'),
('墙面刷漆', '家装', '平方', 35.00, '包含基层处理、腻子、底漆、面漆'),
('地砖铺贴', '家装', '平方', 65.00, '包含找平、铺贴、美缝'),
('吊顶工程', '家装', '平方', 150.00, '包含轻钢龙骨、石膏板、造型'),
('防水工程', '家装', '平方', 80.00, '包含基层处理、防水涂料、闭水试验'),
('拆除工程', '家装', '平方', 40.00, '包含墙体拆除、垃圾清运'),
('墙面贴砖', '家装', '平方', 75.00, '包含基层处理、铺贴、美缝'),
('电路改造', '工装', '平方', 100.00, '包含布线、开关插座安装');

-- ============================================
-- 视图和索引优化建议
-- ============================================

-- 创建订单统计视图（可选）
CREATE OR REPLACE VIEW `v_order_statistics` AS
SELECT 
    DATE_FORMAT(create_time, '%Y-%m') AS month,
    COUNT(*) AS order_count,
    SUM(quote_total) AS total_quote,
    SUM(cost_total) AS total_cost,
    SUM(profit_total) AS total_profit,
    AVG(profit_rate) AS avg_profit_rate
FROM order_main
WHERE order_status != 4
GROUP BY DATE_FORMAT(create_time, '%Y-%m');

-- ============================================
-- 完成提示
-- ============================================
SELECT '数据库建表完成！' AS message;
SELECT '默认管理员账号：admin，密码：admin123' AS tip;
