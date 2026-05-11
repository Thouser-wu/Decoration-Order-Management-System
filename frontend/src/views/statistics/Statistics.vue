<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">数据统计</h1>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="card stat-card">
        <div class="stat-icon orange">
          <Money />
        </div>
        <div class="stat-content">
          <div class="stat-label">本月利润</div>
          <div class="stat-value">¥{{ formatMoney(monthProfit) }}</div>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon green">
          <TrendCharts />
        </div>
        <div class="stat-content">
          <div class="stat-label">本年利润</div>
          <div class="stat-value">¥{{ formatMoney(yearProfit) }}</div>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon blue">
          <Document />
        </div>
        <div class="stat-content">
          <div class="stat-label">本月订单</div>
          <div class="stat-value">{{ monthOrders }} 单</div>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon purple">
          <User />
        </div>
        <div class="stat-content">
          <div class="stat-label">客户总数</div>
          <div class="stat-value">{{ customerCount }} 人</div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <div class="card chart-card">
        <div class="card-title">利润趋势</div>
        <div ref="profitChartRef" style="height: 300px"></div>
      </div>

      <div class="card chart-card">
        <div class="card-title">成本占比</div>
        <div ref="costChartRef" style="height: 300px"></div>
      </div>

      <div class="card chart-card">
        <div class="card-title">订单状态分布</div>
        <div ref="orderChartRef" style="height: 300px"></div>
      </div>

      <div class="card chart-card">
        <div class="card-title">客户价值排行</div>
        <div ref="customerChartRef" style="height: 300px"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Money, TrendCharts, Document, User } from '@element-plus/icons-vue'

const monthProfit = ref(28500)
const yearProfit = ref(328600)
const monthOrders = ref(12)
const customerCount = ref(45)

const profitChartRef = ref(null)
const costChartRef = ref(null)
const orderChartRef = ref(null)
const customerChartRef = ref(null)

const formatMoney = (money) => {
  if (!money && money !== 0) return '0.00'
  return Number(money).toFixed(2)
}

const initCharts = () => {
  // 利润趋势图
  const profitChart = echarts.init(profitChartRef.value)
  profitChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
    yAxis: { type: 'value', name: '金额(元)' },
    series: [{
      data: [28000, 35000, 42000, 38000, 52000, 28500],
      type: 'bar',
      barWidth: '50%',
      itemStyle: { color: '#e6a23c', borderRadius: [8, 8, 0, 0] }
    }]
  })

  // 成本占比图
  const costChart = echarts.init(costChartRef.value)
  costChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '45%'],
      data: [
        { value: 156800, name: '材料成本' },
        { value: 85000, name: '人工成本' }
      ],
      itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 }
    }]
  })

  // 订单状态分布
  const orderChart = echarts.init(orderChartRef.value)
  orderChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} 单 ({d}%)' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '45%'],
      data: [
        { value: 5, name: '待施工' },
        { value: 8, name: '施工中' },
        { value: 15, name: '已完工' },
        { value: 12, name: '已结算' },
        { value: 2, name: '已取消' }
      ],
      itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 }
    }]
  })

  // 客户价值排行
  const customerChart = echarts.init(customerChartRef.value)
  customerChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    xAxis: { type: 'category', data: ['张三', '李四', '王五', '赵六', '钱七'] },
    yAxis: { type: 'value', name: '累计消费(元)' },
    series: [{
      type: 'bar',
      barWidth: '50%',
      data: [85000, 68000, 52000, 45000, 38000],
      itemStyle: { color: '#67c23a', borderRadius: [0, 4, 4, 0] }
    }]
  })
}

onMounted(() => {
  nextTick(() => {
    initCharts()
  })
})
</script>

<style lang="scss" scoped>
.page-container {
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

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;

  .chart-card {
    .card-title {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 16px;
    }
  }
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}
</style>
