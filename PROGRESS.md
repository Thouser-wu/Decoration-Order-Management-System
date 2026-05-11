# 装修接单管理系统 - 开发进度报告

## 更新日期：2025年5月11日

---

## 一、项目概述

本项目是一个面向装修工人的业务管理平台，采用 Spring Boot + Vue3 前后端分离架构，实现**接单管理、材料采购、工程报价、成本核算、利润自动统计**全流程数字化管理。

---

## 二、已完成工作

### 2.1 后端基础架构 ✅

| 模块 | 状态 | 说明 |
|------|------|------|
| Spring Boot 项目 | ✅ | 已创建，JDK 17 + Spring Boot 3.2.0 |
| MyBatis-Plus | ✅ | 已集成，配置分页插件 |
| JWT 认证 | ✅ | 已实现 JWT 工具类和拦截器 |
| CORS 跨域 | ✅ | 已配置 CorsConfig |
| 统一返回封装 | ✅ | Result.java 统一响应格式 |
| 全局异常处理 | ✅ | BusinessException + GlobalExceptionHandler |
| 日志配置 | ✅ | logback-spring.xml 已配置 |
| 数据库 | ⚠️ | 建表 SQL 存于 database/ 目录 |

**后端目录结构：**
```
src/main/java/com/gzu/decoration/
├── common/
│   └── Result.java              # 统一返回结果封装
├── config/
│   ├── CorsConfig.java          # 跨域配置
│   ├── MybatisPlusConfig.java   # MyBatis-Plus配置
│   └── WebMvcConfig.java        # Web配置
├── exception/
│   ├── BusinessException.java   # 业务异常
│   └── GlobalExceptionHandler.java # 全局异常处理
├── interceptor/
│   └── JwtAuthenticationInterceptor.java # JWT拦截器
└── util/
    └── JwtUtil.java             # JWT工具类
```

---

### 2.2 前端项目搭建 ✅

**技术栈：**
- Vue 3.4.x + Vite 5.x
- Element Plus 2.5.x (UI组件库)
- Pinia 2.x (状态管理)
- Vue Router 4.x (路由管理)
- Axios 1.6.x (HTTP请求)
- ECharts 5.4.x (数据可视化)
- NProgress (进度条)
- SCSS (样式预处理)

**项目初始化：**
```bash
npm create vite@latest frontend -- --template vue
npm install element-plus @element-plus/icons-vue pinia vue-router@4 axios echarts dayjs sass nprogress
```

**前端目录结构：**
```
frontend/src/
├── api/
│   ├── index.js       # API接口统一导出
│   └── request.js     # Axios封装 (拦截器、Token)
├── router/
│   └── index.js       # 路由配置 (登录验证、权限控制)
├── stores/
│   └── user.js        # 用户状态管理 (Pinia)
├── styles/
│   └── main.scss      # 全局样式 (主题色变量、通用类)
├── views/
│   ├── login/
│   │   └── Login.vue          # 登录页面
│   ├── layout/
│   │   └── Layout.vue         # 布局框架 (侧边栏+顶部导航)
│   ├── home/
│   │   └── Home.vue           # 首页工作台
│   ├── customer/
│   │   └── CustomerList.vue   # 客户管理
│   ├── order/
│   │   └── OrderList.vue      # 订单管理
│   ├── material/
│   │   └── MaterialList.vue   # 材料管理
│   ├── quote/
│   │   └── QuoteList.vue      # 报价管理 (占位)
│   ├── labor/
│   │   └── LaborList.vue      # 人工成本 (占位)
│   ├── payment/
│   │   └── PaymentList.vue    # 收款管理 (占位)
│   ├── statistics/
│   │   └── Statistics.vue     # 数据统计 (ECharts图表)
│   └── system/
│       └── SystemConfig.vue    # 系统设置
├── App.vue            # 根组件
└── main.js           # 入口文件
```

---

### 2.3 页面功能详情

#### ✅ 登录页面 (Login.vue)
- 账号密码登录
- 记住密码功能
- 动画背景效果
- JWT Token 认证

#### ✅ 布局框架 (Layout.vue)
- 侧边栏导航菜单 (可折叠)
- 顶部导航 (面包屑、用户下拉)
- 路由过渡动画

#### ✅ 首页工作台 (Home.vue)
- 统计卡片：今日订单、本月利润、待收款、库存预警
- 最新订单列表
- 待收款提醒列表
- 库存预警列表

#### ✅ 客户管理 (CustomerList.vue)
- 客户列表 (分页、搜索)
- 新增/编辑客户表单
- 删除客户功能

#### ✅ 订单管理 (OrderList.vue)
- 订单列表 (状态筛选、搜索)
- 显示报价、成本、利润、利润率
- 订单状态和收款状态标签

#### ✅ 材料管理 (MaterialList.vue)
- 材料列表 (分类、名称搜索)
- 库存预警提示 (红色高亮)
- 采购、编辑、删除操作入口

#### ✅ 数据统计 (Statistics.vue)
- 统计卡片：本月利润、本年订单、客户总数
- 利润趋势柱状图 (ECharts)
- 成本占比饼图 (材料/人工)
- 订单状态分布饼图
- 客户价值排行柱状图

#### ✅ 系统设置 (SystemConfig.vue)
- 个人信息修改
- 修改密码
- 系统配置 (公司信息)
- 数据备份/恢复入口

#### ⏳ 占位页面
- 报价管理 (QuoteList.vue)
- 人工成本 (LaborList.vue)
- 收款管理 (PaymentList.vue)

---

### 2.4 配置说明

**vite.config.js - 开发代理配置：**
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

**主题色配置 (styles/main.scss)：**
```scss
--primary-color: #e6a23c;      // 主题橙黄色
--primary-light: #f0c78a;
--primary-dark: #b88236;
```

---

## 三、启动方式

### 前端启动：
```bash
cd frontend
npm run dev
# 访问 http://localhost:5173
```

### 后端启动：
```bash
# 确保 MySQL 数据库已创建并执行建表 SQL
cd Decoration-Order-Management-System
mvn spring-boot:run
# 访问 http://localhost:8080
```

---

## 四、待完成工作

### 4.1 后端 API 开发
- [ ] 客户管理 CRUD 接口
- [ ] 订单管理 CRUD 接口
- [ ] 材料管理 CRUD 接口
- [ ] 人工成本管理接口
- [ ] 报价管理接口
- [ ] 收款管理接口
- [ ] 统计接口 (工作台数据、月度/年度利润)
- [ ] 系统设置接口

### 4.2 前端功能完善
- [ ] 报价管理页面开发
- [ ] 人工成本管理页面开发
- [ ] 收款管理页面开发
- [ ] 订单详情页
- [ ] 客户详情页
- [ ] 材料采购弹窗
- [ ] 订单材料消耗绑定
- [ ] 报价单导出 Excel

### 4.3 数据可视化增强
- [ ] 月度利润趋势图 (近12个月)
- [ ] 年度利润趋势图
- [ ] 成本分析详情
- [ ] 客户价值排行 TOP10

---

## 五、项目资料

- **详细计划书**：[Plan.md](../Plan.md)
- **数据库脚本**：[database/](../database/)
- **项目文档**：[README.md](../README.md)

---

## 六、技术亮点

1. **前后端分离架构**：Spring Boot REST API + Vue3 单页应用
2. **JWT 认证**：无状态 Token 认证，支持 7 天有效期
3. **响应式设计**：Element Plus 组件库，支持多种屏幕尺寸
4. **数据可视化**：ECharts 图表，实时展示利润、成本数据
5. **自动计算**：报价、成本、利润全自动计算，减少人工操作
6. **主题风格**：温暖的橙黄色系，适配装修行业特点

---

## 七、开发建议

1. **后端优先**：先完善 Spring Boot 后端 API，前端可先用模拟数据预览
2. **接口规范**：遵循 RESTful 风格，统一返回格式
3. **组件复用**：抽取通用组件 (表格、表单、弹窗)
4. **代码规范**：ESLint + Prettier 保证代码质量

---

*持续更新中...*
