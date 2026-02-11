<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>普通客人</span>
        <el-button type="primary" style="float: right" @click="openEdit()">新增客人</el-button>
      </template>
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="姓名" clearable style="width: 140px" />
        <el-input v-model="query.phone" placeholder="手机号" clearable style="width: 140px" />
        <el-button type="primary" @click="loadList">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="idTypeName" label="证件类型" width="100" />
        <el-table-column prop="idNo" label="证件号" width="180" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑客人' : '新增客人'" width="480px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
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
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getGuestList, saveGuest, deleteGuest } from '@/api/guest'
import { getIdTypeList } from '@/api/dict'

const list = ref([])
const idTypeList = ref([])
const query = reactive({ name: '', phone: '' })
const dialogVisible = ref(false)
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({ id: null, name: '', phone: '', idTypeId: null, idTypeName: '', idNo: '', remark: '' })
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

async function loadList() {
  try {
    const res = await getGuestList(query)
    console.log('Guest API Response:', res) // 调试信息
    // 根据响应拦截器的返回结构调整数据访问
    const guestData = res?.data?.data || res?.data || res || []
    console.log('Guest Data:', guestData) // 调试信息
    console.log('idTypeList:', idTypeList.value) // 调试信息
    
    list.value = guestData.map(g => ({
      ...g,
      idTypeName: Array.isArray(idTypeList.value) ? idTypeList.value.find(t => t.id === g.idTypeId)?.name || g.idTypeName : g.idTypeName
    }))
    console.log('Processed guest list:', list.value) // 调试信息
  } catch (error) {
    console.error('Load list error:', error)
    ElMessage.error('加载数据失败')
  }
}

async function loadDict() {
  try {
    const res = await getIdTypeList()
    console.log('ID Type API Response:', res) // 调试信息
    // 根据响应拦截器的返回结构调整数据访问
    idTypeList.value = Array.isArray(res?.data) ? res.data : (Array.isArray(res) ? res : [])
    console.log('Processed idTypeList:', idTypeList.value) // 调试信息
  } catch (error) {
    console.error('Load dict error:', error)
    ElMessage.error('加载字典数据失败')
  }
}

function openEdit(row) {
  if (row) {
    Object.assign(form, { id: row.id, name: row.name, phone: row.phone, idTypeId: row.idTypeId, idTypeName: row.idTypeName, idNo: row.idNo, remark: row.remark })
  } else {
    resetForm()
  }
  dialogVisible.value = true
}

function resetForm() {
  // 清空表单数据
  form.id = null
  form.name = ''
  form.phone = ''
  form.idTypeId = null
  form.idTypeName = ''
  form.idNo = ''
  form.remark = ''
  
  // 重置表单验证状态
  nextTick(() => {
    formRef.value?.clearValidate()
    formRef.value?.resetFields()
  })
}

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  const idType = idTypeList.value.find(t => t.id === form.idTypeId)
  submitLoading.value = true
  try {
    await saveGuest({ ...form, idTypeName: idType?.name })
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
  await ElMessageBox.confirm('确定删除该客人档案吗？', '提示', { type: 'warning' })
  await deleteGuest(row.id)
  ElMessage.success('删除成功')
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
