<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>预订管理</span>
        <el-button type="primary" style="float: right" @click="openAdd">新建预订</el-button>
      </template>
      <div class="toolbar">
        <el-input v-model="query.reservationNo" placeholder="预订单号" clearable style="width: 180px" />
        <el-input v-model="query.guestName" placeholder="客人姓名" clearable style="width: 120px" />
        <el-select v-model="query.statusId" placeholder="预订状态" clearable style="width: 120px">
          <el-option v-for="s in statusList" :key="s.id" :label="s.name" :value="s.id" />
        </el-select>
        <el-date-picker v-model="query.checkInDate" type="date" placeholder="入住日期" value-format="YYYY-MM-DD" style="width: 140px" />
        <el-button type="primary" @click="loadList">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="reservationNo" label="预订单号" width="160" />
        <el-table-column prop="roomTypeName" label="房型" width="90" />
        <el-table-column prop="roomNo" label="房号" width="70" />
        <el-table-column prop="channelName" label="渠道" width="80" />
        <el-table-column prop="statusName" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.statusId === 3 ? 'info' : row.statusId === 1 ? 'warning' : 'success'">{{ row.statusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkInDate" label="入住日期" width="110" />
        <el-table-column prop="checkOutDate" label="退房日期" width="110" />
        <el-table-column prop="guestName" label="客人" width="90" />
        <el-table-column prop="guestPhone" label="电话" width="120" />
        <el-table-column prop="roomRate" label="房价" width="80" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.statusId === 1" link type="danger" @click="handleCancel(row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新建预订" width="520px" @close="resetResForm">
      <el-form ref="resFormRef" :model="resForm" :rules="resRules" label-width="100px">
        <el-form-item label="房型" prop="roomTypeId">
          <el-select v-model="resForm.roomTypeId" placeholder="请选择" style="width: 100%">
            <el-option v-for="r in roomTypes" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="渠道" prop="channelId">
          <el-select v-model="resForm.channelId" placeholder="请选择" style="width: 100%">
            <el-option v-for="c in channelList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="入住日期" prop="checkInDate">
          <el-date-picker v-model="resForm.checkInDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="退房日期" prop="checkOutDate">
          <el-date-picker v-model="resForm.checkOutDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="客人姓名" prop="guestName">
          <el-input v-model="resForm.guestName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="客人电话" prop="guestPhone">
          <el-input v-model="resForm.guestPhone" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="证件号" prop="guestIdNo">
          <el-input v-model="resForm.guestIdNo" placeholder="选填" />
        </el-form-item>
        <el-form-item label="房价" prop="roomRate">
          <el-input-number v-model="resForm.roomRate" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="resForm.remark" type="textarea" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitRes">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getReservationList, saveReservation, cancelReservation } from '@/api/reservation'
import { getRoomTypeList } from '@/api/roomType'
import { getReservationStatusList, getReservationChannelList } from '@/api/dict'

const list = ref([])
const statusList = ref([])
const channelList = ref([])
const roomTypes = ref([])
const query = reactive({ reservationNo: '', guestName: '', statusId: null, checkInDate: '' })
const dialogVisible = ref(false)
const resFormRef = ref(null)
const submitLoading = ref(false)

const resForm = reactive({
  roomTypeId: null,
  channelId: 1,
  statusId: 1,  // 默认状态：待入住
  checkInDate: '',
  checkOutDate: '',
  guestName: '',
  guestPhone: '',
  guestIdNo: '',
  roomRate: null,
  remark: ''
})
const resRules = {
  roomTypeId: [{ required: true, message: '请选择房型', trigger: 'change' }],
  checkInDate: [
    { required: true, message: '请选择入住日期', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请选择入住日期'))
        } else if (resForm.checkOutDate && value >= resForm.checkOutDate) {
          callback(new Error('入住日期必须早于退房日期'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  checkOutDate: [
    { required: true, message: '请选择退房日期', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请选择退房日期'))
        } else if (resForm.checkInDate && value <= resForm.checkInDate) {
          callback(new Error('退房日期必须晚于入住日期'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  guestName: [
    { required: true, message: '请输入客人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在 2 到 20 个字符之间', trigger: 'blur' }
  ],
  guestPhone: [
    { required: false, message: '请输入联系电话', trigger: 'blur' },
    { 
      pattern: /^1[3-9]\d{9}$/, 
      message: '请输入正确的手机号码', 
      trigger: 'blur' 
    }
  ],
  roomRate: [
    { required: true, message: '请输入房价', trigger: 'blur' },
    { 
      type: 'number', 
      min: 0.01, 
      message: '房价必须大于0', 
      trigger: 'blur' 
    }
  ]
}

async function loadList() {
  const res = await getReservationList(query)
  list.value = res.list || []
}

async function loadDict() {
  const [s, c, r] = await Promise.all([getReservationStatusList(), getReservationChannelList(), getRoomTypeList()])
  statusList.value = s || []
  channelList.value = c || []
  roomTypes.value = r?.list || r || []
}

function openAdd() {
  resetResForm()
  dialogVisible.value = true
}

function resetResForm() {
  Object.assign(resForm, {
    roomTypeId: null,
    channelId: 1,
    statusId: 1,  // 默认状态：待入住
    checkInDate: '',
    checkOutDate: '',
    guestName: '',
    guestPhone: '',
    guestIdNo: '',
    roomRate: roomTypes.value[0]?.basePrice ?? 298,
    remark: ''
  })
  resFormRef.value?.resetFields()
}

async function submitRes() {
  try {
    await resFormRef.value?.validate()
    submitLoading.value = true
    await saveReservation(resForm)
    ElMessage.success('预订成功')
    dialogVisible.value = false
    loadList()
  } catch (e) {
    // Element Plus 的 validate() 会抛出错误对象，包含错误信息
    if (e instanceof Error) {
      // 这是表单校验错误，Element Plus 已经显示了错误提示
      console.log('表单校验未通过:', e.message)
    } else {
      // 这是网络请求或其他错误
      ElMessage.error(e?.message || '提交失败')
    }
  } finally {
    submitLoading.value = false
  }
}

async function handleCancel(row) {
  await ElMessageBox.confirm('确定取消该预订吗？', '提示', { type: 'warning' })
  await cancelReservation(row.id)
  ElMessage.success('已取消')
  loadList()
}

onMounted(async () => {
  await loadDict()
  await loadList()
})
</script>

<style scoped>
.page { padding: 0; }
.toolbar { margin-bottom: 12px; display: flex; gap: 8px; flex-wrap: wrap; }
</style>
