import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { useUserStore } from '@/stores/user'

NProgress.configure({ showSpinner: false })

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/layout/Layout.vue'),
    redirect: '/home',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/Home.vue'),
        meta: { title: '工作台', icon: 'DataAnalysis' }
      },
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('@/views/customer/CustomerList.vue'),
        meta: { title: '客户管理', icon: 'User' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/order/OrderList.vue'),
        meta: { title: '订单管理', icon: 'Document' }
      },
      {
        path: 'material',
        name: 'Material',
        component: () => import('@/views/material/MaterialList.vue'),
        meta: { title: '材料管理', icon: 'Box' }
      },
      {
        path: 'quote',
        name: 'Quote',
        component: () => import('@/views/quote/QuoteList.vue'),
        meta: { title: '报价管理', icon: 'Tickets' }
      },
      {
        path: 'labor',
        name: 'Labor',
        component: () => import('@/views/labor/LaborList.vue'),
        meta: { title: '人工成本', icon: 'UserFilled' }
      },
      {
        path: 'payment',
        name: 'Payment',
        component: () => import('@/views/payment/PaymentList.vue'),
        meta: { title: '收款管理', icon: 'Money' }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/Statistics.vue'),
        meta: { title: '数据统计', icon: 'TrendCharts' }
      },
      {
        path: 'system',
        name: 'System',
        component: () => import('@/views/system/SystemConfig.vue'),
        meta: { title: '系统设置', icon: 'Setting' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  NProgress.start()

  const userStore = useUserStore()
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth !== false) {
    if (!token) {
      next('/login')
      return
    }
    if (!userStore.userInfo) {
      userStore.getUserInfo()
    }
  }

  if (to.meta.title) {
    document.title = `${to.meta.title} - 装修接单管理系统`
  }

  next()
})

router.afterEach(() => {
  NProgress.done()
})

export default router
