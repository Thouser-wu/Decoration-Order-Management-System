import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, logout, getUserInfo } from '@/api'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  const loginAction = async (username, password) => {
    const res = await login({ username, password })
    if (res.code === 200) {
      token.value = res.data.token
      localStorage.setItem('token', res.data.token)
      return res
    }
    throw new Error(res.message || '登录失败')
  }

  const getUserInfo = async () => {
    if (!token.value) return
    try {
      const res = await getUserInfo()
      if (res.code === 200) {
        userInfo.value = res.data
      }
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }

  const logoutAction = async () => {
    try {
      await logout()
    } catch (error) {
      console.error('登出失败', error)
    } finally {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('token')
      router.push('/login')
    }
  }

  return {
    token,
    userInfo,
    loginAction,
    getUserInfo,
    logoutAction
  }
})
