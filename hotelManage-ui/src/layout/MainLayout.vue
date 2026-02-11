<template>
  <el-container class="main-layout">
    <el-aside width="220px" class="aside">
      <div class="logo">酒店客房管理</div>
      <el-menu
        :default-active="activeMenu"
        :default-openeds="['room-menu', 'stay-menu', 'user-menu', 'system-menu']"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="room-menu">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>客房管理</span>
          </template>
          <el-menu-item index="/room-type">房型管理</el-menu-item>
          <el-menu-item index="/room">房间管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/reservation">
          <el-icon><Calendar /></el-icon>
          <span>预订管理</span>
        </el-menu-item>
        <el-sub-menu index="stay-menu">
          <template #title>
            <el-icon><Key /></el-icon>
            <span>入住退房</span>
          </template>
          <el-menu-item index="/check-in">入住管理</el-menu-item>
          <el-menu-item index="/check-out">退房结账</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="user-menu">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/guest">普通客人</el-menu-item>
          <el-menu-item index="/member">会员档案</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="system-menu">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/role">角色管理</el-menu-item>
          <el-menu-item index="/system-user">系统用户</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container direction="vertical">
      <el-header class="header">
        <span class="title">{{ currentTitle }}</span>
        <div class="right">
          <el-dropdown>
            <span class="el-dropdown-link">
              <el-icon><User /></el-icon>
              {{ userInfo?.username || '用户' }}
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { House, User, ArrowDown, OfficeBuilding, Calendar, Key, UserFilled, Setting } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta?.title || '首页')

const userInfo = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('userInfo') || '{}')
  } catch {
    return {}
  }
})

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/login')
}
</script>

<style scoped>
.main-layout {
  height: 100vh;
}
.aside {
  background-color: #304156;
}
.logo {
  height: 50px;
  line-height: 50px;
  text-align: center;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;
}
.title {
  font-size: 18px;
}
.right .el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}
.main {
  background: #f0f2f5;
  padding: 16px;
  overflow: auto;
}
</style>
