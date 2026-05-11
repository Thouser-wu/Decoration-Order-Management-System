<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">订单管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新建订单
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="card search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.orderStatus" placeholder="请选择状态" clearable>
            <el-option label="待施工" :value="0" />
            <el-option label="施工中" :value="1" />
            <el-option label="已完工" :value="2" />
            <el-option label="已结算" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户姓名" clearable />
        </el-form-item>
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 订单列表 -->
    <div class="card table-card">
      <el-table :data="orderList" v-loading="loading" stripe>
        <el-table-column prop="orderNo" label="订单编号" width="150" />
        <el-table-column prop="customerName" label="客户" min-width="100" />
        <el-table-column prop="projectType" label="装修类型" width="90" />
        <el-table-column prop="quoteTotal" label="报价金额" width="110">
          <template #default="{ row }">
            <span class="amount">¥{{ formatMoney(row.quoteTotal) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="costTotal" label="总成本" width="100">
          <template #default="{ row }">
            <span class="amount">¥{{ formatMoney(row.costTotal) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="profitTotal" label="利润" width="100">
          <template #default="{ row }">
            <span :class="['amount', row.profitTotal >= 0 ? 'positive' : 'negative']">
              ¥{{ formatMoney(row.profitTotal) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="profitRate" label="利润率" width="80">
          <template #default="{ row }">
            <span :class="['profit-rate', getRateClass(row.profitRate)]">
              {{ row.profitRate?.toFixed(1) }}%
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
        <el-table-column prop="paymentStatus" label="收款" width="80">
          <template #default="{ row }">
            <span :class="['payment-tag', row.paymentStatus === 2 ? 'paid' : 'unpaid']">
              {{ getPaymentText(row.paymentStatus) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">详情</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link @click="handleStatusChange(row)">状态</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchList"
          @current-change="fetchList"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderList, updateOrderStatus } from '@/api'
import { Plus, Search } from '@element-plus/icons-vue'

const loading = ref(false)
const orderList = ref([])

const searchForm = reactive({
  orderStatus: null,
  customerName: '',
  orderNo: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

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

const getPaymentText = (status) => {
  const map = { 0: '未收款', 1: '部分收款', 2: '已全款' }
  return map[status] || '未知'
}

const getRateClass = (rate) => {
  if (rate < 0) return 'danger'
  if (rate < 10) return 'warning'
  return 'success'
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getOrderList({ ...searchForm, ...pagination })
    orderList.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error(error)
    // 模拟数据
    orderList.value = [
      { id: 1, orderNo: 'ORD20250511001', customerName: '张三', projectType: '家装', quoteTotal: 35000, costTotal: 26500, profitTotal: 8500, profitRate: 24.3, orderStatus: 1, paymentStatus: 1 },
      { id: 2, orderNo: 'ORD20250511002', customerName: '李四', projectType: '家装', quoteTotal: 28000, costTotal: 21800, profitTotal: 6200, profitRate: 22.1, orderStatus: 0, paymentStatus: 0 },
      { id: 3, orderNo: 'ORD20250510003', customerName: '王五', projectType: '工装', quoteTotal: 52000, costTotal: 36800, profitTotal: 15200, profitRate: 29.2, orderStatus: 2, paymentStatus: 2 }
    ]
    pagination.total = 3
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchList()
}

const handleReset = () => {
  searchForm.orderStatus = null
  searchForm.customerName = ''
  searchForm.orderNo = ''
  handleSearch()
}

const handleAdd = () => {
  ElMessage.info('新建订单功能开发中')
}

const handleView = (row) => {
  console.log('查看订单', row)
}

const handleEdit = (row) => {
  console.log('编辑订单', row)
}

const handleStatusChange = async (row) => {
  console.log('修改状态', row)
}

onMounted(() => {
  fetchList()
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

.search-card {
  margin-bottom: 20px;
}

.table-card {
  .pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}

.profit-rate {
  font-weight: 600;
  &.success { color: #67c23a; }
  &.warning { color: #e6a23c; }
  &.danger { color: #f56c6c; }
}

.payment-tag {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
  &.paid { background: #f0f9eb; color: #67c23a; }
  &.unpaid { background: #fef0f0; color: #f56c6c; }
}
</style>
