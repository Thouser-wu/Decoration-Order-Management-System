<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">系统设置</h1>
    </div>

    <div class="settings-grid">
      <!-- 个人信息 -->
      <div class="card settings-card">
        <div class="card-title">个人信息</div>
        <el-form :model="userForm" label-width="100px">
          <el-form-item label="真实姓名">
            <el-input v-model="userForm.realName" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="userForm.phone" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleUpdateUser">保存</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 修改密码 -->
      <div class="card settings-card">
        <div class="card-title">修改密码</div>
        <el-form :model="passwordForm" label-width="100px">
          <el-form-item label="原密码">
            <el-input v-model="passwordForm.oldPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="passwordForm.newPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 系统配置 -->
      <div class="card settings-card">
        <div class="card-title">系统配置</div>
        <el-form :model="systemForm" label-width="100px">
          <el-form-item label="公司名称">
            <el-input v-model="systemForm.companyName" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="systemForm.companyPhone" />
          </el-form-item>
          <el-form-item label="联系地址">
            <el-input v-model="systemForm.companyAddress" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSaveConfig">保存配置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 数据管理 -->
      <div class="card settings-card">
        <div class="card-title">数据管理</div>
        <div class="data-actions">
          <el-button type="primary" @click="handleBackup">
            <el-icon><Download /></el-icon>数据备份
          </el-button>
          <el-button @click="handleRestore">
            <el-icon><Upload /></el-icon>数据恢复
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Upload } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const userForm = reactive({
  realName: userStore.userInfo?.realName || '',
  phone: userStore.userInfo?.phone || ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const systemForm = reactive({
  companyName: '装修工作室',
  companyPhone: '13800138000',
  companyAddress: '某某市某某区某某路'
})

const handleUpdateUser = () => {
  ElMessage.success('保存成功')
}

const handleChangePassword = () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword) {
    ElMessage.warning('请填写完整信息')
    return
  }
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次密码输入不一致')
    return
  }
  ElMessage.success('密码修改成功')
}

const handleSaveConfig = () => {
  ElMessage.success('配置保存成功')
}

const handleBackup = () => {
  ElMessage.info('数据备份功能开发中')
}

const handleRestore = () => {
  ElMessage.info('数据恢复功能开发中')
}
</script>

<style lang="scss" scoped>
.page-container {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.settings-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.settings-card {
  .card-title {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 20px;
    padding-bottom: 12px;
    border-bottom: 1px solid var(--border-color);
  }
}

.data-actions {
  display: flex;
  gap: 12px;
}

@media (max-width: 1000px) {
  .settings-grid {
    grid-template-columns: 1fr;
  }
}
</style>
