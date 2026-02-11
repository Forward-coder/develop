<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>入住管理</span>
        <el-button type="primary" style="float: right" @click="openCheckIn">办理入住</el-button>
      </template>
      <div class="toolbar">
        <el-input v-model="query.roomNo" placeholder="房号" clearable style="width: 120px" />
        <el-input v-model="query.guestName" placeholder="客人姓名" clearable style="width: 120px" />
        <el-button type="primary" @click="loadList">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="checkInNo" label="入住单号" width="160" />
        <el-table-column prop="roomNo" label="房号" width="80" />
        <el-table-column prop="roomTypeName" label="房型" width="90" />
        <el-table-column prop="guestName" label="客人" width="90" />
        <el-table-column prop="guestPhone" label="电话" width="120" />
        <el-table-column prop="actualCheckInTime" label="入住时间" width="160" />
        <el-table-column prop="plannedCheckOutDate" label="预计退房" width="110" />
        <el-table-column prop="roomRate" label="房价" width="80" />
        <el-table-column prop="depositTotal" label="押金" width="80" />
        <el-table-column prop="consumptionTotal" label="消费" width="80" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="goExtend(row)">续住</el-button>
            <el-button link type="primary" @click="goCheckOut(row)">退房</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="办理入住" width="560px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="房间" prop="roomId">
          <el-select v-model="form.roomId" placeholder="请选择房间" style="width: 100%" filterable>
            <el-option v-for="r in availableRooms" :key="r.id" :label="`${r.roomNo} (${r.roomTypeName})`" :value="r.id">
              <span>{{ r.roomNo }}</span>
              <span style="color: var(--el-text-color-secondary); margin-left: 8px;">{{ r.roomTypeName }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预计退房" prop="plannedCheckOutDate">
          <el-date-picker v-model="form.plannedCheckOutDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="房费(元/间夜)" prop="roomRate">
          <el-input-number v-model="form.roomRate" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="客人姓名" prop="guestName">
          <el-input v-model="form.guestName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="客人电话" prop="guestPhone">
          <el-input v-model="form.guestPhone" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="证件号" prop="guestIdNo">
          <el-input v-model="form.guestIdNo" placeholder="选填" />
        </el-form-item>
        <el-form-item label="押金(元)" prop="depositTotal">
          <el-input-number v-model="form.depositTotal" :min="0" :precision="2" style="width: 100%" />
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

    <el-dialog v-model="extendVisible" title="续住" width="400px">
      <el-form label-width="100px">
        <el-form-item label="房号">{{ extendRow?.roomNo }}</el-form-item>
        <el-form-item label="新退房日期">
          <el-date-picker v-model="extendDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="extendVisible = false">取消</el-button>
        <el-button type="primary" @click="submitExtend">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCheckInList, doCheckIn, extendStay } from '@/api/checkIn'
import { getRoomList } from '@/api/room'

const router = useRouter()
const list = ref([])
const availableRooms = ref([])
const query = reactive({ roomNo: '', guestName: '' })
const dialogVisible = ref(false)
const extendVisible = ref(false)
const extendRow = ref(null)
const extendDate = ref('')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  roomId: null,
  roomNo: '',
  roomTypeId: null,
  roomTypeName: '',
  plannedCheckOutDate: '',
  roomRate: 298,
  guestName: '',
  guestPhone: '',
  guestIdNo: '',
  depositTotal: 0,
  remark: ''
})

const rules = {
  roomId: [{ required: true, message: '请选择房间', trigger: 'change' }],
  plannedCheckOutDate: [{ required: true, message: '请选择预计退房日期', trigger: 'change' }],
  roomRate: [{ required: true, message: '请输入房费', trigger: 'blur' }],
  guestName: [{ required: true, message: '请输入客人姓名', trigger: 'blur' }]
}

async function loadList() {
  try {
    const params = { status: 'staying' }
    if (query.roomNo) params.roomNo = query.roomNo
    if (query.guestName) params.guestName = query.guestName
    
    const res = await getCheckInList(params)
    // 直接使用res，因为响应拦截器已经返回了实际的数据
    list.value = res || []
  } catch (error) {
    console.error('加载入住列表失败:', error)
    list.value = []
  }
}

async function loadAvailableRooms() {
  try {
    const res = await getRoomList({ roomStatusId: 1 })
    // 直接使用res.list，因为getRoomList函数返回的是{ list: [...], total: ... }
    availableRooms.value = res.list || []
  } catch (error) {
    console.error('加载可用房间失败:', error)
    availableRooms.value = []
  }
}

function openCheckIn() {
  loadAvailableRooms()
  resetForm()
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, {
    roomId: null,
    roomNo: '',
    roomTypeId: null,
    roomTypeName: '',
    plannedCheckOutDate: '',
    roomRate: 298,
    guestName: '',
    guestPhone: '',
    guestIdNo: '',
    depositTotal: 0,
    remark: ''
  })
  formRef.value?.resetFields()
}

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  const room = availableRooms.value.find(r => r.id === form.roomId)
  if (!room) return
  submitLoading.value = true
  try {
    await doCheckIn({
      roomId: form.roomId,
      roomNo: room.roomNo,
      roomTypeId: room.roomTypeId,
      roomTypeName: room.roomTypeName,
      plannedCheckOutDate: form.plannedCheckOutDate,
      roomRate: form.roomRate,
      guestName: form.guestName,
      guestPhone: form.guestPhone,
      guestIdNo: form.guestIdNo,
      depositTotal: form.depositTotal,
      remark: form.remark
    })
    ElMessage.success('入住成功')
    dialogVisible.value = false
    loadList()
    loadAvailableRooms()
  } catch (e) {
    ElMessage.error(e?.message || '办理失败')
  } finally {
    submitLoading.value = false
  }
}

function goExtend(row) {
  extendRow.value = row
  extendDate.value = row.plannedCheckOutDate
  extendVisible.value = true
}

async function submitExtend() {
  if (!extendDate.value) {
    ElMessage.warning('请选择新退房日期')
    return
  }
  
  try {
    await extendStay(extendRow.value.id, extendDate.value)
    ElMessage.success('续住成功')
    extendVisible.value = false
    loadList()
  } catch (error) {
    ElMessage.error(error?.message || '续住失败')
  }
}

function goCheckOut(row) {
  router.push({ path: '/check-out', query: { id: row.id } })
}

onMounted(() => {
  loadList()
})
</script>

<style scoped>
.page { padding: 0; }
.toolbar { margin-bottom: 12px; display: flex; gap: 8px; flex-wrap: wrap; }
</style>
