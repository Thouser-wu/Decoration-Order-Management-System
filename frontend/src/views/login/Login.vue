<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
      <div class="bg-shape shape-3"></div>
    </div>

    <div class="login-card">
      <div class="login-header">
        <div class="logo">
          <el-icon :size="40" color="#e6a23c"><House /></el-icon>
        </div>
        <h1>装修接单管理系统</h1>
        <p>高效管理 · 智能报价 · 精准核算</p>
      </div>

      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <div class="login-options">
            <el-checkbox v-model="loginForm.remember">记住密码</el-checkbox>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <p>默认账号：admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { House } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      await userStore.loginAction(loginForm.username, loginForm.password)
      ElMessage.success('登录成功')
      router.push('/home')
    } catch (error) {
      console.error(error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.6;
}

.shape-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, rgba(230, 162, 60, 0.15), rgba(230, 162, 60, 0.05));
  top: -100px;
  right: -100px;
  animation: float 8s ease-in-out infinite;
}

.shape-2 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, rgba(103, 194, 58, 0.1), rgba(103, 194, 58, 0.03));
  bottom: -50px;
  left: -50px;
  animation: float 10s ease-in-out infinite reverse;
}

.shape-3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.08), rgba(64, 158, 255, 0.02));
  top: 50%;
  left: 30%;
  animation: float 12s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(5deg); }
}

.login-card {
  width: 420px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  padding: 48px 40px;
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;

  .logo {
    width: 80px;
    height: 80px;
    background: linear-gradient(135deg, #f0c78a, #e6a23c);
    border-radius: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 20px;
    box-shadow: 0 8px 24px rgba(230, 162, 60, 0.3);
  }

  h1 {
    font-size: 24px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 8px;
  }

  p {
    font-size: 14px;
    color: #909399;
  }
}

.login-form {
  :deep(.el-input__wrapper) {
    padding: 4px 12px;
    border-radius: 8px;
  }

  :deep(.el-input__wrapper:focus-within) {
    box-shadow: 0 0 0 2px rgba(230, 162, 60, 0.2);
  }
}

.login-options {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  background: linear-gradient(135deg, #e6a23c, #b88236);
  border: none;

  &:hover {
    background: linear-gradient(135deg, #b88236, #e6a23c);
  }
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;

  p {
    font-size: 12px;
    color: #909399;
  }
}
</style>
