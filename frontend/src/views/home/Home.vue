<template>
  <div class="home-page">
    <div class="page-header">
      <h1 class="page-title">工作台</h1>
      <div class="header-actions">
        <el-button type="primary" @click="router.push('/order')">
          <el-icon><Plus /></el-icon>新建订单
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="card stat-card">
        <div class="stat-icon orange">
          <Document />
        </div>
        <div class="stat-content">
          <div class="stat-label">今日新增订单</div>
          <div class="stat-value">{{ dashboard.todayOrders || 0 }}</div>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon green">
          <Money />
        </div>
        <div class="stat-content">
          <div class="stat-label">本月利润</div>
          <div class="stat-value">¥{{ formatMoney(dashboard.monthProfit) }}</div>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon blue">
          <Wallet />
        </div>
        <div class="stat-content">
          <div class="stat-label">待收款金额</div>
          <div class="stat-value warning">¥{{ formatMoney(dashboard.pendingPayment) }}</div>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon red" @click="router.push('/material')" style="cursor: pointer">
          <Warning />
        </div>
        <div class="stat-content">
          <div class="stat-label">库存预警</div>
          <div class="stat-value">{{ dashboard.stockAlerts || 0 }} 项</div>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-grid">
      <!-- 订单列表 -->
      <div class="card order-section">
        <div class="card-header">
          <h3 class="card-title">最新订单</h3>
          <el-button text type="primary" @click="router.push('/order')">查看全部</el-button>
        </div>
        <el-table :data="dashboard.recentOrders" style="width: 100%">
          <el-table-column prop="orderNo" label="订单号" width="140" />
          <el-table-column prop="customerName" label="客户" min-width="100" />
          <el-table-column prop="quoteTotal" label="报价金额" width="110">
            <template #default="{ row }">
              <span class="amount">¥{{ formatMoney(row.quoteTotal) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="profitTotal" label="利润" width="100">
            <template #default="{ row }">
              <span :class="['amount', row.profitTotal >= 0 ? 'positive' : 'negative']">
                ¥{{ formatMoney(row.profitTotal) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="orderStatus" label="状态" width="90">
            <template #default="{ row }">
              <span :class="['status-tag', getStatusClass(row.orderStatus)]">
                {{ getStatusText(row.orderStatus) }}
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 待收款 -->
      <div class="card payment-section">
        <div class="card-header">
          <h3 class="card-title">待收款提醒</h3>
          <el-button text type="primary" @click="router.push('/payment')">查看全部</el-button>
        </div>
        <div class="payment-list" v-if="dashboard.pendingPayments?.length">
          <div
            class="payment-item"
            v-for="item in dashboard.pendingPayments"
            :key="item.id"
            @click="router.push('/order')"
          >
            <div class="payment-info">
              <div class="customer-name">{{ item.customerName }}</div>
              <div class="order-no">{{ item.orderNo }}</div>
            </div>
            <div class="payment-amount">
              <div class="amount">¥{{ formatMoney(item.arrearsAmount) }}</div>
              <div class="payment-date">逾期{{ item.overdueDays }}天</div>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无待收款" :image-size="80" />
      </div>

      <!-- 库存预警 -->
      <div class="card stock-section">
        <div class="card-header">
          <h3 class="card-title">库存预警</h3>
          <el-button text type="primary" @click="router.push('/material')">补货</el-button>
        </div>
        <div class="stock-list" v-if="dashboard.stockAlertList?.length">
          <div class="stock-item" v-for="item in dashboard.stockAlertList" :key="item.id">
            <div class="stock-info">
              <div class="material-name">{{ item.materialName }}</div>
              <div class="material-type">{{ item.materialType }}</div>
            </div>
            <div class="stock-num">
              <span class="current">{{ item.stockNum }}</span>
              <span class="min">/ 最低 {{ item.minStock }}</span>
            </div>
          </div>
        </div>
        <el-empty v-else description="库存充足" :image-size="80" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDashboard } from '@/api'
import { Plus, Document, Money, Wallet, Warning } from '@element-plus/icons-vue'

const router = useRouter()

const dashboard = ref({})

const formatMoney = (money) => {
  if (!money && money !== 0) return '0.00'
  return Number(money).toFixed(2)
}

const getStatusClass = (status) => {
  const map = { 0: 'pending', 1: 'processing', 2: 'completed', 3: 'settled', 4: 'cancelled' }
  return map[status] || 'pending'
}

const getStatusText = (status) => {
  const map = { 0: '待施工', 1: '施工中', 2: '已完工', 3: '已结算', 4: '已取消' }
  return map[status] || '未知'
}

const fetchDashboard = async () => {
  try {
    const res = await getDashboard()
    dashboard.value = res.data || {}
  } catch (error) {
    console.error('获取工作台数据失败', error)
    // 模拟数据
    dashboard.value = {
      todayOrders: 2,
      monthProfit: 28500,
      pendingPayment: 15600,
      stockAlerts: 3,
      recentOrders: [
        { id: 1, orderNo: 'ORD20250511001', customerName: '张三', quoteTotal: 35000, profitTotal: 8500, orderStatus: 1 },
        { id: 2, orderNo: 'ORD20250511002', customerName: '李四', quoteTotal: 28000, profitTotal: 6200, orderStatus: 0 },
        { id: 3, orderNo: 'ORD20250510003', customerName: '王五', quoteTotal: 52000, profitTotal: 15200, orderStatus: 2 }
      ],
      pendingPayments: [
        { id: 1, customerName: '张三', orderNo: 'ORD20250505001', arrearsAmount: 15000, overdueDays: 5 }
      ],
      stockAlertList: [
        { id: 1, materialName: '水泥', materialType: '水电材料', stockNum: 5, minStock: 20 },
        { id: 2, materialName: '电线', materialType: '水电材料', stockNum: 50, minStock: 100 },
        { id: 3, materialName: '乳胶漆', materialType: '墙面材料', stockNum: 3, minStock: 10 }
      ]
    }
  }
}

onMounted(() => {
  fetchDashboard()
})
</script>

<style lang="scss" scoped>
.home-page {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
}

.content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  grid-template-rows: auto auto;
  gap: 20px;

  .order-section {
    grid-column: 1 / 2;
    grid-row: 1 / 2;
  }

  .payment-section {
    grid-column: 2 / 3;
    grid-row: 1 / 3;
  }

  .stock-section {
    grid-column: 1 / 2;
    grid-row: 2 / 3;
  }
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;

  .card-title {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.payment-list {
  .payment-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.3s;
    margin-bottom: 8px;

    &:hover {
      background: #f5f7fa;
    }

    .payment-info {
      .customer-name {
        font-size: 14px;
        font-weight: 500;
        color: var(--text-primary);
      }

      .order-no {
        font-size: 12px;
        color: var(--text-secondary);
        margin-top: 4px;
      }
    }

    .payment-amount {
      text-align: right;

      .amount {
        font-size: 16px;
        font-weight: 600;
        color: var(--danger-color);
      }

      .payment-date {
        font-size: 12px;
        color: var(--danger-color);
        margin-top: 4px;
      }
    }
  }
}

.stock-list {
  .stock-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px;
    border-radius: 8px;
    background: #fef0f0;
    margin-bottom: 8px;

    .stock-info {
      .material-name {
        font-size: 14px;
        font-weight: 500;
        color: var(--text-primary);
      }

      .material-type {
        font-size: 12px;
        color: var(--text-secondary);
        margin-top: 4px;
      }
    }

    .stock-num {
      .current {
        font-size: 16px;
        font-weight: 600;
        color: var(--danger-color);
      }

      .min {
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .content-grid {
    grid-template-columns: 1fr;

    .order-section,
    .payment-section,
    .stock-section {
      grid-column: 1;
      grid-row: auto;
    }
  }
}
</style>
