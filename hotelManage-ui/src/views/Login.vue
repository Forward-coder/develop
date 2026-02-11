<template>
  <div class="login-page">
    <div class="login-card">
      <h1 class="title">酒店客房管理系统</h1>
      <el-form ref="formRef" :model="form" :rules="rules" class="form" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="用户名"
            size="large"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            size="large"
            show-password
            :prefix-icon="Lock"
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" class="btn-login" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  await formRef.value?.validate().catch(() => {})
  loading.value = true
  try {
    // 预留：调用登录接口后写入 token 与 userInfo
    // const res = await loginApi(form)
    // localStorage.setItem('token', res.token)
    // localStorage.setItem('userInfo', JSON.stringify(res.userInfo))
    localStorage.setItem('token', 'mock-token')
    localStorage.setItem('userInfo', JSON.stringify({ username: form.username }))
    ElMessage.success('登录成功')
    router.push('/')
  } catch (e) {
    ElMessage.error(e?.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}
.title {
  text-align: center;
  margin-bottom: 32px;
  font-size: 22px;
  color: #303133;
}
.form {
  margin-top: 20px;
}
.btn-login {
  width: 100%;
}
</style>
