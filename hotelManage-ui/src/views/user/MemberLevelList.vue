<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>会员等级管理</span>
        <el-button type="primary" style="float: right" @click="openEdit()">新增等级</el-button>
      </template>
      <el-table :data="list" border stripe>
        <el-table-column prop="name" label="等级名称" width="120" />
        <el-table-column prop="discountRate" label="折扣率" width="100">
          <template #default="{ row }">
            {{ (row.discountRate * 100).toFixed(0) }}%
          </template>
        </el-table-column>
        <el-table-column prop="minPoints" label="最低积分" width="120" />
        <el-table-column prop="isEnabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled ? 'success' : 'danger'">
              {{ row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button 
              link 
              :type="row.isEnabled ? 'danger' : 'success'" 
              @click="toggleStatus(row)"
            >
              {{ row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑会员等级' : '新增会员等级'" width="450px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="等级名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入等级名称" />
        </el-form-item>
        <el-form-item label="折扣率" prop="discountRate">
          <el-input-number 
            v-model="form.discountRate" 
            :min="0.1" 
            :max="1" 
            :step="0.05" 
            :precision="2"
            style="width: 100%" 
          />
          <div style="font-size: 12px; color: #999; margin-top: 5px;">
            例如：0.95 表示 95折
          </div>
        </el-form-item>
        <el-form-item label="最低积分" prop="minPoints">
          <el-input-number 
            v-model="form.minPoints" 
            :min="0" 
            style="width: 100%" 
          />
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-switch v-model="form.isEnabled" :active-value="true" :inactive-value="false" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMemberLevelList, saveMemberLevel, toggleMemberLevelStatus } from '@/api/member'

const list = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  id: null,
  name: '',
  discountRate: 1.00,
  minPoints: 0,
  isEnabled: true
})

const rules = {
  name: [{ required: true, message: '请输入等级名称', trigger: 'blur' }],
  discountRate: [{ required: true, message: '请输入折扣率', trigger: 'blur' }],
  minPoints: [{ required: true, message: '请输入最低积分', trigger: 'blur' }]
}

async function loadList() {
  try {
    const res = await getMemberLevelList()
    // 根据响应拦截器的返回结构调整数据访问
    list.value = res?.data?.data || res?.data || res || []
  } catch (error) {
    console.error('加载会员等级列表失败:', error)
    ElMessage.error('加载数据失败')
  }
}

function openEdit(row) {
  if (row) {
    Object.assign(form, { 
      id: row.id, 
      name: row.name, 
      discountRate: row.discountRate, 
      minPoints: row.minPoints, 
      isEnabled: row.isEnabled 
    })
  } else {
    resetForm()
  }
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, {
    id: null,
    name: '',
    discountRate: 1.00,
    minPoints: 0,
    isEnabled: true
  })
  formRef.value?.resetFields()
}

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  submitLoading.value = true
  try {
    await saveMemberLevel({ ...form })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadList()
  } catch (e) {
    ElMessage.error(e?.message || '保存失败')
  } finally {
    submitLoading.value = false
  }
}

async function toggleStatus(row) {
  try {
    await ElMessageBox.confirm(
      `确定要${row.isEnabled ? '禁用' : '启用'}该会员等级吗？`, 
      '提示', 
      { type: 'warning' }
    )
    await toggleMemberLevelStatus(row.id, !row.isEnabled)
    ElMessage.success(`${row.isEnabled ? '禁用' : '启用'}成功`)
    loadList()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(e?.message || '操作失败')
    }
  }
}

onMounted(() => {
  loadList()
})
</script>

<style scoped>
.page { padding: 0; }
</style>