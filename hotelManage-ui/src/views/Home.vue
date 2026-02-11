<template>
  <div class="home">
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <el-icon class="stat-icon" :size="32"><OfficeBuilding /></el-icon>
            <div>
              <div class="stat-value">{{ stats.roomTotal }}</div>
              <div class="stat-label">房间总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <el-icon class="stat-icon vacant" :size="32"><Key /></el-icon>
            <div>
              <div class="stat-value">{{ stats.vacantCount }}</div>
              <div class="stat-label">空房</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <el-icon class="stat-icon occupied" :size="32"><User /></el-icon>
            <div>
              <div class="stat-value">{{ stats.occupiedCount }}</div>
              <div class="stat-label">在住</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <el-icon class="stat-icon" :size="32"><Calendar /></el-icon>
            <div>
              <div class="stat-value">{{ stats.reservationToday }}</div>
              <div class="stat-label">今日预抵</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="12">
        <el-card>
          <template #header>快捷入口</template>
          <div class="quick-links">
            <el-button type="primary" @click="$router.push('/reservation')">新建预订</el-button>
            <el-button type="success" @click="$router.push('/check-in')">办理入住</el-button>
            <el-button @click="$router.push('/room')">房间管理</el-button>
            <el-button @click="$router.push('/room-type')">房型管理</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>欢迎使用</template>
          <p>酒店客房管理系统：房型与房间管理、预订、入住、退房结账等。请从左侧菜单进入对应功能。</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { OfficeBuilding, Key, User, Calendar } from '@element-plus/icons-vue'
import { getRoomList } from '@/api/room'
import { getCheckInList } from '@/api/checkIn'
import { getReservationList } from '@/api/reservation'

const stats = ref({
  roomTotal: 0,
  vacantCount: 0,
  occupiedCount: 0,
  reservationToday: 0
})

async function loadStats() {
  try {
    const today = new Date().toISOString().slice(0, 10)
    const [roomRes, checkInRes, resvRes] = await Promise.all([
      getRoomList(),
      getCheckInList({ status: 'staying' }),
      getReservationList({ checkInDate: today })
    ])
    const rooms = roomRes.list || []
    const staying = checkInRes.list || []
    const todayResv = (resvRes.list || []).filter(r => r.statusId === 1 && r.checkInDate === today)
    stats.value = {
      roomTotal: rooms.length,
      vacantCount: rooms.filter(r => r.roomStatusId === 1).length,
      occupiedCount: staying.length,
      reservationToday: todayResv.length
    }
  } catch {
    stats.value = { roomTotal: 0, vacantCount: 0, occupiedCount: 0, reservationToday: 0 }
  }
}

onMounted(loadStats)
</script>

<style scoped>
.home {
  min-height: 100%;
}
.stats-row {
  margin-bottom: 0;
}
.stat-card {
  margin-bottom: 0;
}
.stat-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}
.stat-icon {
  color: #409eff;
}
.stat-icon.vacant {
  color: #67c23a;
}
.stat-icon.occupied {
  color: #e6a23c;
}
.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}
.quick-links {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
.quick-links .el-button {
  margin: 0;
}
</style>
