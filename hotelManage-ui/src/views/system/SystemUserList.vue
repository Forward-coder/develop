<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>系统用户</span>
        <el-button type="primary" style="float: right" @click="openEdit()">新增用户</el-button>
      </template>
      <div class="toolbar">
        <el-input v-model="query.username" placeholder="登录名" clearable style="width: 140px" />
        <el-input v-model="query.realName" placeholder="姓名" clearable style="width: 140px" />
        <el-button type="primary" @click="loadList">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="username" label="登录名" width="120" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="roleNames" label="角色" width="140" />
        <el-table-column prop="isEnabled" label="状态" width="90">
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="480px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="登录名" prop="username">
          <el-input v-model="form.username" placeholder="用于登录" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="选填" />
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="form.roleIds" multiple placeholder="请选择角色" style="width: 100%">
            <el-option v-for="r in roleList" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
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
import { getSystemUserList, saveSystemUser, deleteSystemUser } from '@/api/systemUser'
import { getRoleList } from '@/api/role'

const list = ref([])
const roleList = ref([])
const query = reactive({ username: '', realName: '' })
const dialogVisible = ref(false)
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({ id: null, username: '', realName: '', phone: '', roleIds: [], roleNames: '', isEnabled: 1 })
const rules = {
  username: [{ required: true, message: '请输入登录名', trigger: 'blur' }],
  roleIds: [{ required: true, message: '请至少选择一个角色', trigger: 'change', type: 'array', min: 1 }]
}

async function loadList() {
  const res = await getSystemUserList(query)
  list.value = res.list || []
}

async function loadRoles() {
  const res = await getRoleList()
  roleList.value = res.list || []
}

function openEdit(row) {
  if (row) {
    Object.assign(form, {
      id: row.id,
      username: row.username,
      realName: row.realName,
      phone: row.phone || '',
      roleIds: row.roleIds || (row.roleNames ? [] : []),
      roleNames: row.roleNames,
      isEnabled: row.isEnabled ?? 1
    })
    if (!form.roleIds.length && row.roleNames) {
      const names = row.roleNames.split(/[,，]/)
      form.roleIds = roleList.value.filter(r => names.includes(r.name)).map(r => r.id)
    }
  } else {
    resetForm()
  }
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, { id: null, username: '', realName: '', phone: '', roleIds: [], roleNames: '', isEnabled: 1 })
  formRef.value?.resetFields()
}

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  const roleNames = roleList.value.filter(r => form.roleIds.includes(r.id)).map(r => r.name).join(',')
  submitLoading.value = true
  try {
    await saveSystemUser({ ...form, roleNames })
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
  await ElMessageBox.confirm('确定删除该用户吗？', '提示', { type: 'warning' })
  await deleteSystemUser(row.id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(async () => {
  await loadRoles()
  await loadList()
})
</script>

<style scoped>
.page { padding: 0; }
.toolbar { margin-bottom: 12px; display: flex; gap: 8px; flex-wrap: wrap; }
</style>
