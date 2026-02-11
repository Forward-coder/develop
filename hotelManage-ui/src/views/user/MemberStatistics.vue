<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>会员统计报表</span>
      </template>
      
      <!-- 统计概览 -->
      <div class="overview-stats">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-icon" style="background-color: #409eff;">
                  <el-icon><User /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-title">总会员数</div>
                  <div class="stat-number">{{ statistics.totalMembers }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-icon" style="background-color: #67c23a;">
                  <el-icon><Check /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-title">活跃会员</div>
                  <div class="stat-number">{{ statistics.activeMembers }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-icon" style="background-color: #e6a23c;">
                  <el-icon><Star /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-title">平均积分</div>
                  <div class="stat-number">{{ statistics.averagePoints }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-icon" style="background-color: #f56c6c;">
                  <el-icon><TrendCharts /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-title">本月新增</div>
                  <div class="stat-number">{{ statistics.monthlyGrowth }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-container">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>会员等级分布</span>
              </template>
              <div ref="levelChartRef" style="height: 300px;"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>月度增长趋势</span>
              </template>
              <div ref="growthChartRef" style="height: 300px;"></div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>积分分布</span>
              </template>
              <div ref="pointsChartRef" style="height: 300px;"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>状态分布</span>
              </template>
              <div ref="statusChartRef" style="height: 300px;"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 详细统计数据表格 -->
      <div style="margin-top: 20px;">
        <el-card>
          <template #header>
            <span>详细统计</span>
            <el-button style="float: right;" @click="exportStatistics">导出报表</el-button>
          </template>
          
          <el-tabs v-model="activeTab">
            <el-tab-pane label="等级统计" name="level">
              <el-table :data="levelStats" border>
                <el-table-column prop="levelName" label="会员等级" />
                <el-table-column prop="count" label="会员数量" />
                <el-table-column prop="percentage" label="占比">
                  <template #default="{ row }">
                    {{ row.percentage }}%
                  </template>
                </el-table-column>
                <el-table-column prop="avgPoints" label="平均积分" />
              </el-table>
            </el-tab-pane>
            
            <el-tab-pane label="月度统计" name="monthly">
              <el-table :data="monthlyStats" border>
                <el-table-column prop="month" label="月份" />
                <el-table-column prop="newMembers" label="新增会员" />
                <el-table-column prop="totalMembers" label="累计会员" />
                <el-table-column prop="growthRate" label="增长率">
                  <template #default="{ row }">
                    {{ row.growthRate }}%
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            
            <el-tab-pane label="积分排行" name="points">
              <el-table :data="topMembers" border>
                <el-table-column prop="rank" label="排名" width="80" />
                <el-table-column prop="memberNo" label="会员卡号" />
                <el-table-column prop="name" label="姓名" />
                <el-table-column prop="points" label="积分" sortable />
                <el-table-column prop="levelName" label="等级" />
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Check, Star, TrendCharts } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getMemberStatistics, getLevelDistribution, getMonthlyGrowth, getPointsRanking } from '@/api/member'

const activeTab = ref('level')
const levelChartRef = ref()
const growthChartRef = ref()
const pointsChartRef = ref()
const statusChartRef = ref()

// 统计数据
const statistics = reactive({
  totalMembers: 0,
  activeMembers: 0,
  averagePoints: 0,
  monthlyGrowth: 0
})

// 详细统计数据
const levelStats = ref([])
const monthlyStats = ref([])
const topMembers = ref([])

async function loadStatistics() {
  try {
    // 加载总体统计
    const statsRes = await getMemberStatistics()
    Object.assign(statistics, statsRes.data || statsRes)
    
    // 加载等级分布
    const levelRes = await getLevelDistribution()
    levelStats.value = levelRes.data || levelRes || []
    
    // 加载月度增长
    const monthlyRes = await getMonthlyGrowth()
    monthlyStats.value = monthlyRes.data || monthlyRes || []
    
    // 加载积分排行
    const rankingRes = await getPointsRanking()
    topMembers.value = Array.isArray(rankingRes.data || rankingRes) ? 
      (rankingRes.data || rankingRes).map((member, index) => ({
        ...member,
        rank: index + 1
      })) : []
    
    // 渲染图表
    await nextTick()
    renderLevelChart()
    renderGrowthChart()
    renderPointsChart()
    renderStatusChart()
    
  } catch (error) {
    console.error('加载统计失败:', error)
    ElMessage.error('加载统计数据失败')
  }
}

function renderLevelChart() {
  if (!levelChartRef.value) return
  
  const chart = echarts.init(levelChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      name: '会员等级',
      type: 'pie',
      radius: ['40%', '70%'],
      data: levelStats.value.map(item => ({
        name: item.levelName,
        value: item.count
      })),
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
  chart.setOption(option)
}

function renderGrowthChart() {
  if (!growthChartRef.value || !Array.isArray(monthlyStats.value)) return
  
  const chart = echarts.init(growthChartRef.value)
  const months = monthlyStats.value.map(item => item.month)
  const newMembers = monthlyStats.value.map(item => item.newMembers)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: months
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      name: '新增会员',
      type: 'line',
      data: newMembers,
      smooth: true,
      areaStyle: {}
    }]
  }
  chart.setOption(option)
}

function renderPointsChart() {
  if (!pointsChartRef.value) return
  
  const chart = echarts.init(pointsChartRef.value)
  const ranges = ['0-1000', '1001-5000', '5001-10000', '10001-50000', '50000+']
  const data = [30, 25, 20, 15, 10] // 示例数据
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ranges
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      name: '会员数量',
      type: 'bar',
      data: data,
      itemStyle: {
        color: '#409eff'
      }
    }]
  }
  chart.setOption(option)
}

function renderStatusChart() {
  if (!statusChartRef.value) return
  
  const chart = echarts.init(statusChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: 'bottom'
    },
    series: [{
      name: '会员状态',
      type: 'pie',
      radius: '60%',
      data: [
        { value: statistics.activeMembers, name: '正常' },
        { value: statistics.totalMembers - statistics.activeMembers, name: '非正常' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
  chart.setOption(option)
}

function exportStatistics() {
  ElMessage.info('报表导出功能开发中...')
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.page { padding: 0; }
.overview-stats { margin-bottom: 20px; }
.stat-card {
  height: 120px;
}
.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}
.stat-icon .el-icon {
  font-size: 24px;
  color: white;
}
.stat-info {
  flex: 1;
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}
.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
.charts-container { margin: 20px 0; }
</style>