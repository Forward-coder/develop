<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>房型管理</span>
        <el-button type="primary" style="float: right" @click="openEdit()">新增房型</el-button>
      </template>
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="房型名称" clearable style="width: 160px" @keyup.enter="loadList" />
        <el-select v-model="query.bedTypeId" placeholder="床型" clearable style="width: 120px" @change="loadList">
          <el-option v-for="item in bedTypes" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-button type="primary" @click="loadList">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="code" label="房型编码" width="100" />
        <el-table-column prop="name" label="房型名称" width="120" />
        <el-table-column prop="area" label="面积(㎡)" width="90" />
        <el-table-column prop="bedTypeName" label="床型" width="90" />
        <el-table-column prop="maxGuests" label="可住人数" width="90" />
        <el-table-column prop="basePrice" label="基础价格(元)" width="120" />
        <el-table-column prop="sortOrder" label="排序" width="70" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑房型' : '新增房型'" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="房型编码" prop="code">
          <el-input v-model="form.code" placeholder="如 STD" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="房型名称" prop="name">
          <el-input v-model="form.name" placeholder="如 标准间" />
        </el-form-item>
        <el-form-item label="面积(㎡)" prop="area">
          <el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="床型" prop="bedTypeId">
          <el-select v-model="form.bedTypeId" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in bedTypes" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="可住人数" prop="maxGuests">
          <el-input-number v-model="form.maxGuests" :min="1" :max="10" style="width: 100%" />
        </el-form-item>
        <el-form-item label="基础价格" prop="basePrice">
          <el-input-number v-model="form.basePrice" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
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
import { getRoomTypeList, saveRoomType, deleteRoomType } from '@/api/roomType'
import { getBedTypes } from '@/api/dict'

const list = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const submitLoading = ref(false)
const bedTypes = ref([])
const query = reactive({ name: '', bedTypeId: null })

const form = reactive({
  id: null,
  code: '',
  name: '',
  area: null,
  bedTypeId: null,
  maxGuests: 2,
  basePrice: null,
  sortOrder: 0
})

const rules = {
  code: [{ required: true, message: '请输入房型编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入房型名称', trigger: 'blur' }],
  bedTypeId: [{ required: true, message: '请选择床型', trigger: 'change' }],
  maxGuests: [{ required: true, message: '请输入可住人数', trigger: 'blur' }],
  basePrice: [{ required: true, message: '请输入基础价格', trigger: 'blur' }]
}

async function loadList() {
  const res = await getRoomTypeList(query)
  list.value = res.list || []
}

async function loadDict() {
  bedTypes.value = await getBedTypes()
}

function openEdit(row) {
  if (row) {
    Object.assign(form, { id: row.id, code: row.code, name: row.name, area: row.area, bedTypeId: row.bedTypeId, maxGuests: row.maxGuests, basePrice: row.basePrice, sortOrder: row.sortOrder ?? 0 })
  } else {
    resetForm()
  }
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, { id: null, code: '', name: '', area: null, bedTypeId: null, maxGuests: 2, basePrice: null, sortOrder: 0 })
  formRef.value?.resetFields()
}

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  submitLoading.value = true
  try {
    await saveRoomType(form)
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
  await ElMessageBox.confirm('确定删除该房型吗？', '提示', { type: 'warning' })
  await deleteRoomType(row.id)
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
