<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>角色管理</span>
        <el-button type="primary" style="float: right" @click="openEdit()">新增角色</el-button>
      </template>
      <el-table :data="list" border stripe>
        <el-table-column prop="code" label="角色编码" width="120" />
        <el-table-column prop="name" label="角色名称" width="140" />
        <el-table-column prop="isEnabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEnabled === 1 ? 'success' : 'info'">{{ row.isEnabled === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑角色' : '新增角色'" width="420px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="如 admin" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="如 管理员" />
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-radio-group v-model="form.isEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
import { getRoleList, saveRole, deleteRole } from '@/api/role'

const list = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({ id: null, code: '', name: '', isEnabled: 1 })
const rules = {
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
}

async function loadList() {
  const res = await getRoleList()
  list.value = res.list || []
}

function openEdit(row) {
  if (row) {
    Object.assign(form, { id: row.id, code: row.code, name: row.name, isEnabled: row.isEnabled ?? 1 })
  } else {
    resetForm()
  }
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, { id: null, code: '', name: '', isEnabled: 1 })
  formRef.value?.resetFields()
}

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  submitLoading.value = true
  try {
    await saveRole(form)
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
  await ElMessageBox.confirm('确定删除该角色吗？', '提示', { type: 'warning' })
  await deleteRole(row.id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>

<style scoped>
.page { padding: 0; }
</style>
