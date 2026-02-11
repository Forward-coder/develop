import axios from 'axios'
import { ElMessage } from 'element-plus'

// 直接使用相对路径，避免环境变量问题
const request = axios.create({
  baseURL: '',  // 空字符串，让请求使用相对路径
  timeout: 15000
})

request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (err) => Promise.reject(err)
)

request.interceptors.response.use(
  (res) => {
    const { data } = res
    if (data?.code !== undefined && data.code !== 0 && data.code !== 200) {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    // 返回完整的响应对象，保持原有的数据结构
    return res
  },
  (err) => {
    const msg = err.response?.data?.message || err.message || '网络错误'
    ElMessage.error(msg)
    if (err.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export default request