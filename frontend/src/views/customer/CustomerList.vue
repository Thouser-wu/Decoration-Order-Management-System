<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">客户管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增客户
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="card search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户姓名" clearable />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="searchForm.customerPhone" placeholder="请输入联系电话" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 客户列表 -->
    <div class="card table-card">
      <el-table :data="customerList" v-loading="loading" stripe>
        <el-table-column prop="customerName" label="客户姓名" min-width="100" />
        <el-table-column prop="customerPhone" label="联系电话" width="120" />
        <el-table-column prop="address" label="装修地址" min-width="150" />
        <el-table-column prop="houseType" label="房屋类型" width="100" />
        <el-table-column prop="area" label="面积(㎡)" width="80" />
        <el-table-column prop="source" label="客户来源" width="100" />
        <el-table-column prop="orderCount" label="订单数" width="80" />
        <el-table-column prop="createTime" label="登记时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="customerPhone">
          <el-input v-model="form.customerPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="装修地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入装修地址" />
        </el-form-item>
        <el-form-item label="房屋类型" prop="houseType">
          <el-select v-model="form.houseType" placeholder="请选择房屋类型">
            <el-option label="新房" value="新房" />
            <el-option label="二手房" value="二手房" />
            <el-option label="商铺" value="商铺" />
          </el-select>
        </el-form-item>
        <el-form-item label="房屋面积" prop="area">
          <el-input-number v-model="form.area" :min="0" :precision="2" placeholder="请输入房屋面积" />
        </el-form-item>
        <el-form-item label="客户来源" prop="source">
          <el-select v-model="form.source" placeholder="请选择客户来源">
            <el-option label="转介绍" value="转介绍" />
            <el-option label="网络平台" value="网络平台" />
            <el-option label="线下" value="线下" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCustomerList, addCustomer, updateCustomer, deleteCustomer } from '@/api'
import { Plus, Search } from '@element-plus/icons-vue'

const loading = ref(false)
const customerList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const submitLoading = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  customerName: '',
  customerPhone: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  customerName: '',
  customerPhone: '',
  address: '',
  houseType: '',
  area: null,
  source: '',
  remark: ''
})

const rules = {
  customerName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  customerPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getCustomerList({ ...searchForm, ...pagination })
    customerList.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error(error)
    // 模拟数据
    customerList.value = [
      { id: 1, customerName: '张三', customerPhone: '13800138000', address: '阳光小区A栋101', houseType: '新房', area: 120, source: '转介绍', orderCount: 2, createTime: '2025-05-01 10:00:00' },
      { id: 2, customerName: '李四', customerPhone: '13900139000', address: '华府花园B栋202', houseType: '二手房', area: 95, source: '网络平台', orderCount: 1, createTime: '2025-05-05 14:30:00' }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchList()
}

const handleReset = () => {
  searchForm.customerName = ''
  searchForm.customerPhone = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增客户'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑客户'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  // 跳转到客户详情
  console.log('查看客户', row)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该客户吗？', '提示', { type: 'warning' })
    await deleteCustomer(row.id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (form.id) {
        await updateCustomer(form.id, form)
        ElMessage.success('编辑成功')
      } else {
        await addCustomer(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchList()
    } catch (error) {
      console.error(error)
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.assign(form, { id: null, customerName: '', customerPhone: '', address: '', houseType: '', area: null, source: '', remark: '' })
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

:deep(.el-select) {
  width: 100%;
}
</style>
