<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>会员档案</span>
        <el-button type="primary" style="float: right" @click="openEdit()">新增会员</el-button>
      </template>
      
      <!-- 统计信息栏 -->
      <div class="stats-bar" style="margin-bottom: 15px; padding: 10px; background: #f0f9ff; border-radius: 4px;">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-item">
              <span class="stat-label">总会员数:</span>
              <span class="stat-value">{{ list.length }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <span class="stat-label">正常会员:</span>
              <span class="stat-value" style="color: #67c23a;">{{ list.filter(m => m.status === 1).length }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <span class="stat-label">平均积分:</span>
              <span class="stat-value">{{ averagePoints }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <span class="stat-label">今日新增:</span>
              <span class="stat-value" style="color: #409eff;">{{ todayMembers }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 搜索工具栏 -->
      <div class="toolbar">
        <el-row :gutter="10">
          <el-col :span="4">
            <el-input v-model="query.memberNo" placeholder="会员卡号" clearable />
          </el-col>
          <el-col :span="3">
            <el-input v-model="query.name" placeholder="姓名" clearable />
          </el-col>
          <el-col :span="4">
            <el-input v-model="query.phone" placeholder="手机号" clearable />
          </el-col>
          <el-col :span="4">
            <el-input v-model="query.idNo" placeholder="证件号" clearable />
          </el-col>
          <el-col :span="3">
            <el-select v-model="query.levelId" placeholder="会员等级" clearable>
              <el-option 
                v-for="level in memberLevelList" 
                :key="level.id" 
                :label="level.name" 
                :value="level.id" 
              />
            </el-select>
          </el-col>
          <el-col :span="3">
            <el-select v-model="query.status" placeholder="状态" clearable>
              <el-option label="正常" :value="1" />
              <el-option label="冻结" :value="2" />
              <el-option label="黑名单" :value="3" />
            </el-select>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" @click="loadList">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-col>
        </el-row>
        
        <!-- 高级筛选 -->
        <div style="margin-top: 10px;">
          <el-button link type="primary" @click="showAdvancedFilter = !showAdvancedFilter">
            {{ showAdvancedFilter ? '收起高级筛选' : '展开高级筛选' }}
            <el-icon><ArrowDown v-if="!showAdvancedFilter" /><ArrowUp v-else /></el-icon>
          </el-button>
        </div>
        
        <div v-show="showAdvancedFilter" style="margin-top: 10px; padding: 10px; background: #f5f7fa; border-radius: 4px;">
          <el-row :gutter="10">
            <el-col :span="6">
              <el-form-item label="积分范围" label-width="80px">
                <el-col :span="11">
                  <el-input-number v-model="query.minPoints" :min="0" placeholder="最小积分" style="width: 100%" />
                </el-col>
                <el-col :span="2" style="text-align: center;">-</el-col>
                <el-col :span="11">
                  <el-input-number v-model="query.maxPoints" :min="0" placeholder="最大积分" style="width: 100%" />
                </el-col>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="注册时间" label-width="80px">
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button type="success" @click="exportMembers">导出Excel</el-button>
            </el-col>
          </el-row>
        </div>
      </div>
      
      <!-- 会员列表 -->
      <el-table :data="list" border stripe>
        <el-table-column prop="memberNo" label="会员卡号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="levelName" label="会员等级" width="100" />
        <el-table-column prop="points" label="积分" width="90" sortable />
        <el-table-column prop="statusName" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'warning' : 'danger'">
              {{ row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="success" @click="openAdjustPoints(row)">调积分</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑会员对话框 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑会员' : '新增会员'" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item v-if="form.id" label="会员卡号">
          <el-input v-model="form.memberNo" disabled />
        </el-form-item>
        <el-form-item v-else label="会员卡号">
          <el-input v-model="generatedMemberNo" disabled placeholder="系统自动生成">
            <template #append>
              <el-button @click="refreshMemberNo">刷新</el-button>
            </template>
          </el-input>
          <div style="font-size: 12px; color: #999; margin-top: 5px;">
            系统将自动生成唯一会员卡号
          </div>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="证件类型" prop="idTypeId">
          <el-select v-model="form.idTypeId" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in idTypeList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="证件号" prop="idNo">
          <el-input v-model="form.idNo" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="会员等级" prop="levelId">
          <el-select v-model="form.levelId" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in memberLevelList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="积分" prop="points">
          <el-input-number v-model="form.points" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择" style="width: 100%">
            <el-option label="正常" :value="1" />
            <el-option label="冻结" :value="2" />
            <el-option label="黑名单" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown, ArrowUp } from '@element-plus/icons-vue'
import { getMemberList, saveMember, deleteMember, getMemberLevelList, adjustPoints } from '@/api/member'
import { getIdTypeList } from '@/api/dict'

const list = ref([])
const idTypeList = ref([])
const memberLevelList = ref([])
const query = reactive({ 
  memberNo: '', 
  name: '', 
  phone: '', 
  idNo: '',
  levelId: null,
  status: '',
  minPoints: null,
  maxPoints: null,
  startDate: '',
  endDate: ''
})
const showAdvancedFilter = ref(false)
const dateRange = ref([])
const dialogVisible = ref(false)
const pointsDialogVisible = ref(false)
const selectedMember = ref({})
const formRef = ref(null)
const pointsFormRef = ref(null)
const submitLoading = ref(false)
const pointsLoading = ref(false)
const generatedMemberNo = ref('')

const form = reactive({
  id: null,
  memberNo: '',
  name: '',
  phone: '',
  idTypeId: null,
  idTypeName: '',
  idNo: '',
  levelId: null,
  levelName: '',
  points: 0,
  status: 1,
  remark: ''
})

const pointsForm = reactive({
  type: 'add',
  points: 0,
  remark: ''
})

const pointsRules = {
  points: [{ required: true, message: '请输入积分数', trigger: 'blur' }]
}
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

// 计算属性
const averagePoints = computed(() => {
  if (!Array.isArray(list.value) || list.value.length === 0) return 0
  const total = list.value.reduce((sum, member) => sum + (member.points || 0), 0)
  return Math.round(total / list.value.length)
})

const todayMembers = computed(() => {
  if (!Array.isArray(list.value)) return 0
  const today = new Date().toISOString().split('T')[0]
  return list.value.filter(member => 
    member.createdAt && member.createdAt.startsWith(today)
  ).length
})

async function loadList() {
  try {
    const res = await getMemberList(query)
    console.log('Member API Response:', res)
    // 根据响应拦截器的返回结构调整数据访问
    const memberData = res?.data?.data || res?.data || res || []
    
    list.value = memberData.map(m => ({
      ...m,
      idTypeName: Array.isArray(idTypeList.value) ? idTypeList.value.find(t => t.id === m.idTypeId)?.name || m.idTypeName : m.idTypeName,
      levelName: Array.isArray(memberLevelList.value) ? memberLevelList.value.find(l => l.id === m.levelId)?.name || m.levelName : m.levelName
    }))
  } catch (error) {
    console.error('Load member list error:', error)
    ElMessage.error('加载会员数据失败')
  }
}

async function loadDict() {
  try {
    const [idTypeRes, levelRes] = await Promise.all([
      getIdTypeList(),
      getMemberLevelList()
    ])
    
    // 根据响应拦截器的返回结构调整数据访问
    idTypeList.value = Array.isArray(idTypeRes?.data) ? idTypeRes.data : (Array.isArray(idTypeRes) ? idTypeRes : [])
    memberLevelList.value = Array.isArray(levelRes?.data) ? levelRes.data : (Array.isArray(levelRes) ? levelRes : [])
  } catch (error) {
    console.error('Load dict error:', error)
    ElMessage.error('加载字典数据失败')
  }
}

function openEdit(row) {
  if (row) {
    Object.assign(form, { 
      id: row.id, 
      memberNo: row.memberNo, 
      name: row.name, 
      phone: row.phone, 
      idTypeId: row.idTypeId, 
      idTypeName: row.idTypeName, 
      idNo: row.idNo, 
      levelId: row.levelId, 
      levelName: row.levelName,
      points: row.points ?? 0, 
      status: row.status ?? 1, 
      remark: row.remark 
    })
    generatedMemberNo.value = ''
  } else {
    resetForm()
    generatePreviewMemberNo()
  }
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, { 
    id: null, 
    memberNo: '', 
    name: '', 
    phone: '', 
    idTypeId: null, 
    idTypeName: '', 
    idNo: '', 
    levelId: null,
    levelName: '',
    points: 0, 
    status: 1, 
    remark: '' 
  })
  generatedMemberNo.value = ''
  formRef.value?.resetFields()
}

// 生成预览会员卡号
async function generatePreviewMemberNo() {
  try {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    const dateStr = `${year}${month}${day}`
    const randomSeq = Math.floor(Math.random() * 1000000)
    generatedMemberNo.value = `M${dateStr}${String(randomSeq).padStart(6, '0')}`
  } catch (error) {
    console.error('生成预览卡号失败:', error)
    generatedMemberNo.value = '生成失败'
  }
}

// 刷新会员卡号
function refreshMemberNo() {
  generatePreviewMemberNo()
  ElMessage.info('会员卡号已刷新')
}

// 重置查询条件
function resetQuery() {
  Object.assign(query, {
    memberNo: '',
    name: '',
    phone: '',
    idNo: '',
    levelId: null,
    status: '',
    minPoints: null,
    maxPoints: null,
    startDate: '',
    endDate: ''
  })
  dateRange.value = []
  showAdvancedFilter.value = false
  loadList()
}

// 导出会员数据
function exportMembers() {
  ElMessage.info('导出功能开发中...')
}

// 监听日期范围变化
watch(dateRange, (newVal) => {
  if (newVal && newVal.length === 2) {
    query.startDate = newVal[0]
    query.endDate = newVal[1]
  } else {
    query.startDate = ''
    query.endDate = ''
  }
})

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  const idType = idTypeList.value.find(t => t.id === form.idTypeId)
  submitLoading.value = true
  try {
    const submitData = { ...form, idTypeName: idType?.name }
    if (!form.id && !form.memberNo && generatedMemberNo.value) {
      submitData.memberNo = generatedMemberNo.value
    }
    
    await saveMember(submitData)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadList()
  } catch (e) {
    ElMessage.error(e?.message || '保存失败')
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除该会员档案吗？', '提示', { type: 'warning' })
  await deleteMember(row.id)
  ElMessage.success('删除成功')
  loadList()
}

function openAdjustPoints(row) {
  selectedMember.value = row
  pointsForm.type = 'add'
  pointsForm.points = 0
  pointsForm.remark = ''
  pointsDialogVisible.value = true
}

async function submitPointsAdjustment() {
  await pointsFormRef.value?.validate().catch(() => {})
  pointsLoading.value = true
  try {
    const adjustment = pointsForm.type === 'add' ? pointsForm.points : -pointsForm.points
    await adjustPoints(selectedMember.value.id, adjustment, pointsForm.remark)
    ElMessage.success('积分调整成功')
    pointsDialogVisible.value = false
    loadList()
  } catch (e) {
    ElMessage.error(e?.message || '积分调整失败')
  } finally {
    pointsLoading.value = false
  }
}

onMounted(async () => {
  await loadDict()
  await loadList()
})
</script>

<style scoped>
.page { padding: 0; }
.toolbar { margin-bottom: 12px; }
.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.stat-label {
  font-size: 14px;
  color: #606266;
}
.stat-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
</style>