<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>退房结账</span>
      </template>
      
      <!-- 待退房客人列表 -->
      <div class="toolbar">
        <el-button type="primary" @click="handleRefreshClick">刷新列表</el-button>
      </div>
      
      <el-table :data="pendingList" border stripe @row-click="selectGuest">
        <el-table-column prop="checkInNo" label="入住单号" width="160" />
        <el-table-column prop="roomNo" label="房号" width="80" />
        <el-table-column prop="roomTypeName" label="房型" width="90" />
        <el-table-column prop="guestName" label="客人" width="90" />
        <el-table-column prop="guestPhone" label="电话" width="120" />
        <el-table-column prop="actualCheckInTime" label="入住时间" width="160" />
        <el-table-column prop="plannedCheckOutDate" label="预计退房" width="110" />
        <el-table-column prop="roomRate" label="房价" width="80" />
        <el-table-column prop="depositTotal" label="押金" width="80" />
      </el-table>
    </el-card>
    
    <!-- 退房结账对话框 -->
    <el-dialog v-model="dialogVisible" title="退房结账" width="700px" @close="resetForm">
      <div v-if="selectedCheckIn">
        <!-- 客人信息 -->
        <el-descriptions :column="3" border>
          <el-descriptions-item label="入住单号">{{ selectedCheckIn.checkInNo }}</el-descriptions-item>
          <el-descriptions-item label="房号">{{ selectedCheckIn.roomNo }}</el-descriptions-item>
          <el-descriptions-item label="房型">{{ selectedCheckIn.roomTypeName }}</el-descriptions-item>
          <el-descriptions-item label="客人姓名">{{ selectedCheckIn.guestName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ selectedCheckIn.guestPhone }}</el-descriptions-item>
          <el-descriptions-item label="入住时间">{{ formatDate(selectedCheckIn.actualCheckInTime) }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 账单信息 -->
        <div class="bill-section" v-if="billInfo">
          <h3>账单信息</h3>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-statistic title="房费" :value="billInfo.roomFee" prefix="¥" />
            </el-col>
            <el-col :span="8">
              <el-statistic title="消费费用" :value="billInfo.consumptionFee" prefix="¥" />
            </el-col>
            <el-col :span="8">
              <el-statistic title="总金额" :value="billInfo.totalAmount" prefix="¥" />
            </el-col>
          </el-row>
          
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="8">
              <el-statistic title="押金" :value="billInfo.depositTotal" prefix="¥" />
            </el-col>
            <el-col :span="8">
              <el-statistic title="应收金额" :value="billInfo.totalAmount" prefix="¥" />
            </el-col>
          </el-row>
        </div>
        
        <!-- 结账表单 -->
        <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="margin-top: 20px;">
          <el-form-item label="押金处理" prop="depositHandling">
            <el-radio-group v-model="form.depositHandling">
              <el-radio label="refund">退还押金</el-radio>
              <el-radio label="excess">补缴差额</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="实际收款" prop="actualPaid">
            <el-input-number v-model="form.actualPaid" :min="0" :precision="2" style="width: 200px;" />
            <span style="margin-left: 10px;">元</span>
          </el-form-item>
          
          <el-form-item label="找零" v-if="changeAmount !== null">
            <el-statistic :value="changeAmount" prefix="¥" :value-style="{ color: changeAmount >= 0 ? '#67c23a' : '#f56c6c' }" />
          </el-form-item>
          
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" rows="2" />
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitCheckout">确认退房</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPendingCheckoutList, getCheckoutPreview, doCheckout } from '@/api/checkout'

const pendingList = ref([])
const dialogVisible = ref(false)
const selectedCheckIn = ref(null)
const billInfo = ref(null)
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  checkInId: null,
  depositHandling: 'refund',
  actualPaid: 0,
  remark: ''
})

const rules = {
  depositHandling: [{ required: true, message: '请选择押金处理方式', trigger: 'change' }],
  actualPaid: [{ required: true, message: '请输入实际收款金额', trigger: 'blur' }]
}

// 计算找零金额
const changeAmount = computed(() => {
  if (!billInfo.value || form.actualPaid === null) return null
  
  let change = 0
  if (form.depositHandling === 'refund') {
    // 退款：实际支付 - 总金额 + 押金
    change = form.actualPaid - billInfo.value.totalAmount + billInfo.value.depositTotal
  } else {
    // 补缴：实际支付 - 总金额
    change = form.actualPaid - billInfo.value.totalAmount
  }
  return parseFloat(change.toFixed(2))
})

async function loadPendingList() {
  try {
    console.log('=== 刷新操作开始 ===')
    
    const res = await getPendingCheckoutList()
    console.log('API响应:', res)
    
    // 根据响应拦截器的返回结构调整数据访问
    if (Array.isArray(res?.data)) {
      pendingList.value = res.data
    } else if (Array.isArray(res?.data?.data)) {
      pendingList.value = res.data.data
    } else if (Array.isArray(res)) {
      pendingList.value = res
    } else {
      pendingList.value = []
    }
    
    // 显示成功提示
    ElMessage.success('数据已刷新')
    console.log('=== 刷新操作完成 ===')
  } catch (error) {
    console.error('刷新失败:', error)
    ElMessage.error('刷新失败，请检查网络连接')
  }
}

// 添加测试点击函数
function handleRefreshClick() {
  console.log('刷新按钮被点击了！')
  loadPendingList()
}

function selectGuest(row) {
  selectedCheckIn.value = row
  form.checkInId = row.id
  loadBillPreview(row.id)
  dialogVisible.value = true
}

async function loadBillPreview(checkInId) {
  try {
    const res = await getCheckoutPreview(checkInId)
    // 根据响应拦截器的返回结构调整数据访问
    const billData = res?.data?.bill || res?.bill || res?.data
    billInfo.value = billData
    form.actualPaid = billData?.totalAmount || 0
  } catch (error) {
    console.error('加载账单预览失败:', error)
    ElMessage.error('加载账单预览失败')
  }
}

function resetForm() {
  selectedCheckIn.value = null
  billInfo.value = null
  form.checkInId = null
  form.depositHandling = 'refund'
  form.actualPaid = 0
  form.remark = ''
  formRef.value?.resetFields()
}

async function submitCheckout() {
  await formRef.value?.validate().catch(() => {})
  
  submitLoading.value = true
  try {
    const requestData = {
      checkInId: form.checkInId,
      depositHandling: form.depositHandling,
      actualPaid: form.actualPaid,
      remark: form.remark
    }
    
    const res = await doCheckout(requestData)
    
    // 根据响应拦截器的返回结构调整数据访问
    const result = res?.data?.result || res?.result || res
    if (result?.success) {
      ElMessage.success('退房结账成功')
      dialogVisible.value = false
      loadPendingList()
    } else {
      ElMessage.error(result?.message || '退房结账失败')
    }
  } catch (error) {
    console.error('退房结账失败:', error)
    ElMessage.error(error?.message || '退房结账失败')
  } finally {
    submitLoading.value = false
  }
}

function formatDate(dateString) {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

onMounted(() => {
  loadPendingList()
})
</script>

<style scoped>
.page {
  padding: 0;
}

.toolbar {
  margin-bottom: 12px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.bill-section {
  margin: 20px 0;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.bill-section h3 {
  margin: 0 0 15px 0;
  color: #303133;
}
</style>