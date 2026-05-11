<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <div class="logo" v-show="!isCollapsed">
          <el-icon :size="28" color="#e6a23c"><House /></el-icon>
          <span>装修管理</span>
        </div>
        <div class="logo-mini" v-show="isCollapsed">
          <el-icon :size="28" color="#e6a23c"><House /></el-icon>
        </div>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapsed"
        background-color="#2d3748"
        text-color="#a0aec0"
        active-text-color="#e6a23c"
        router
      >
        <el-menu-item index="/home">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>工作台</template>
        </el-menu-item>
        <el-menu-item index="/customer">
          <el-icon><User /></el-icon>
          <template #title>客户管理</template>
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><Document /></el-icon>
          <template #title>订单管理</template>
        </el-menu-item>
        <el-menu-item index="/material">
          <el-icon><Box /></el-icon>
          <template #title>材料管理</template>
        </el-menu-item>
        <el-menu-item index="/quote">
          <el-icon><Tickets /></el-icon>
          <template #title>报价管理</template>
        </el-menu-item>
        <el-menu-item index="/labor">
          <el-icon><UserFilled /></el-icon>
          <template #title>人工成本</template>
        </el-menu-item>
        <el-menu-item index="/payment">
          <el-icon><Money /></el-icon>
          <template #title>收款管理</template>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><TrendCharts /></el-icon>
          <template #title>数据统计</template>
        </el-menu-item>
        <el-menu-item index="/system">
          <el-icon><Setting /></el-icon>
          <template #title>系统设置</template>
        </el-menu-item>
      </el-menu>
    </aside>

    <!-- 右侧内容区 -->
    <div class="main-container">
      <!-- 顶部导航 -->
      <header class="top-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapsed = !isCollapsed">
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentRoute.meta.title">
              {{ currentRoute.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="32" style="background: #e6a23c">
                {{ userStore.userInfo?.realName?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="username">{{ userStore.userInfo?.realName || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="settings">系统设置</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 内容区域 -->
      <main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { House, DataAnalysis, User, Document, Box, Tickets, UserFilled, Money, TrendCharts, Setting, Fold, Expand, ArrowDown } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapsed = ref(false)
const activeMenu = computed(() => route.path)
const currentRoute = computed(() => route)

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/system')
      break
    case 'settings':
      router.push('/system')
      break
    case 'logout':
      userStore.logoutAction()
      break
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 220px;
  background: #2d3748;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 100;

  &.collapsed {
    width: 64px;
  }
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);

  .logo {
    display: flex;
    align-items: center;
    gap: 10px;
    color: #fff;
    font-size: 18px;
    font-weight: 600;
  }

  .logo-mini {
    color: #fff;
  }
}

.sidebar-menu {
  flex: 1;
  border-right: none;
  overflow-y: auto;

  &:not(.el-menu--collapse) {
    width: 220px;
  }

  :deep(.el-menu-item) {
    height: 50px;
    line-height: 50px;
    margin: 4px 8px;
    border-radius: 8px;

    &:hover {
      background: rgba(255, 255, 255, 0.1) !important;
    }

    &.is-active {
      background: rgba(230, 162, 60, 0.2) !important;
      color: #e6a23c !important;
    }
  }
}

.main-container {
  flex: 1;
  margin-left: 220px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  transition: margin-left 0.3s ease;

  .sidebar.collapsed + & {
    margin-left: 64px;
  }
}

.top-header {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 50;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;

  .collapse-btn {
    font-size: 20px;
    cursor: pointer;
    color: #606266;
    transition: color 0.3s;

    &:hover {
      color: #e6a23c;
    }
  }
}

.header-right {
  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 8px;
    transition: background 0.3s;

    &:hover {
      background: #f5f7fa;
    }

    .username {
      font-size: 14px;
      color: #303133;
    }
  }
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
