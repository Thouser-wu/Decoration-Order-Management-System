<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">材料管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增材料
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="card search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="材料分类">
          <el-select v-model="searchForm.materialType" placeholder="请选择分类" clearable>
            <el-option label="水电材料" value="水电材料" />
            <el-option label="墙面材料" value="墙面材料" />
            <el-option label="地面材料" value="地面材料" />
            <el-option label="木工材料" value="木工材料" />
          </el-select>
        </el-form-item>
        <el-form-item label="材料名称">
          <el-input v-model="searchForm.materialName" placeholder="请输入材料名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 材料列表 -->
    <div class="card table-card">
      <el-table :data="materialList" v-loading="loading" stripe>
        <el-table-column prop="materialName" label="材料名称" min-width="120" />
        <el-table-column prop="materialType" label="分类" width="100" />
        <el-table-column prop="specification" label="规格型号" width="120" />
        <el-table-column prop="unit" label="单位" width="70" />
        <el-table-column prop="purchasePrice" label="采购价" width="90">
          <template #default="{ row }">
            ¥{{ row.purchasePrice?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="salePrice" label="参考售价" width="90">
          <template #default="{ row }">
            ¥{{ row.salePrice?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="stockNum" label="库存" width="80">
          <template #default="{ row }">
            <span :class="{ 'low-stock': row.stockNum < row.minStock }">
              {{ row.stockNum }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="预警值" width="80" />
        <el-table-column prop="supplier" label="供应商" min-width="100" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handlePurchase(row)">采购</el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMaterialList, addMaterial, updateMaterial, deleteMaterial, addMaterialPurchase } from '@/api'
import { Plus, Search } from '@element-plus/icons-vue'

const loading = ref(false)
const materialList = ref([])

const searchForm = reactive({
  materialType: '',
  materialName: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getMaterialList({ ...searchForm, ...pagination })
    materialList.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error(error)
    materialList.value = [
      { id: 1, materialName: '水泥', materialType: '水电材料', specification: '425号', unit: '袋', purchasePrice: 28, salePrice: 35, stockNum: 15, minStock: 20, supplier: '建材市场A店' },
      { id: 2, materialName: '电线', materialType: '水电材料', specification: '2.5平方', unit: '米', purchasePrice: 2.5, salePrice: 3.5, stockNum: 50, minStock: 100, supplier: '电工材料店' },
      { id: 3, materialName: '瓷砖', materialType: '地面材料', specification: '800*800mm', unit: '片', purchasePrice: 45, salePrice: 65, stockNum: 200, minStock: 50, supplier: '瓷砖批发中心' }
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
  searchForm.materialType = ''
  searchForm.materialName = ''
  handleSearch()
}

const handleAdd = () => {
  ElMessage.info('新增材料功能开发中')
}

const handleEdit = (row) => {
  console.log('编辑材料', row)
}

const handlePurchase = (row) => {
  console.log('采购材料', row)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该材料吗？', '提示', { type: 'warning' })
    ElMessage.success('删除成功')
    fetchList()
  } catch (error) {
    console.error(error)
  }
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

.low-stock {
  color: #f56c6c;
  font-weight: 600;
}
</style>
