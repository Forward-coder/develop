<template>
  <div class="page">
    <el-card>
      <template #header>
        <span>房间管理</span>
        <el-button type="primary" style="float: right" @click="openEdit()">新增房间</el-button>
      </template>
      <div class="toolbar">
        <el-select v-model="query.floorId" placeholder="楼层" clearable style="width: 120px" @change="loadList">
          <el-option v-for="f in floors" :key="f.id" :label="f.name" :value="f.id" />
        </el-select>
        <el-select v-model="query.roomTypeId" placeholder="房型" clearable style="width: 120px" @change="loadList">
          <el-option v-for="r in roomTypes" :key="r.id" :label="r.name" :value="r.id" />
        </el-select>
        <el-select v-model="query.roomStatusId" placeholder="状态" clearable style="width: 120px" @change="loadList">
          <el-option v-for="s in roomStatusList" :key="s.id" :label="s.name" :value="s.id" />
        </el-select>
        <el-input v-model="query.roomNo" placeholder="房号" clearable style="width: 120px" @keyup.enter="loadList" />
        <el-button type="primary" @click="loadList">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="roomNo" label="房号" width="80" />
        <el-table-column prop="floorName" label="楼层" width="80" />
        <el-table-column prop="roomTypeName" label="房型" width="100" />
        <el-table-column prop="roomStatusName" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="roomStatusTag(row.roomStatusId)">{{ row.roomStatusName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="openStatus(row)">改状态</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑房间' : '新增房间'" width="480px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="房号" prop="roomNo">
          <el-input v-model="form.roomNo" placeholder="如 101" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="楼层" prop="floorId">
          <el-select v-model="form.floorId" placeholder="请选择" style="width: 100%">
            <el-option v-for="f in floors" :key="f.id" :label="f.name" :value="f.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="房型" prop="roomTypeId">
          <el-select v-model="form.roomTypeId" placeholder="请选择" style="width: 100%">
            <el-option v-for="r in roomTypes" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="roomStatusId" v-if="form.id">
          <el-select v-model="form.roomStatusId" placeholder="请选择" style="width: 100%">
            <el-option v-for="s in roomStatusList" :key="s.id" :label="s.name" :value="s.id" />
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

    <el-dialog v-model="statusDialogVisible" title="修改房间状态" width="360px">
      <el-form label-width="80px">
        <el-form-item label="房号">{{ currentRoom?.roomNo }}</el-form-item>
        <el-form-item label="状态">
          <el-select v-model="statusForm.roomStatusId" placeholder="请选择" style="width: 100%">
            <el-option v-for="s in roomStatusList" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStatus">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoomList, getFloorList, saveRoom, deleteRoom, updateRoomStatus } from '@/api/room'
import { getRoomTypeList } from '@/api/roomType'
import { getRoomStatusList } from '@/api/dict'

const list = ref([])
const floors = ref([])
const roomTypes = ref([])
const roomStatusList = ref([])
const query = reactive({ floorId: null, roomTypeId: null, roomStatusId: null, roomNo: '' })
const dialogVisible = ref(false)
const statusDialogVisible = ref(false)
const formRef = ref(null)
const submitLoading = ref(false)
const currentRoom = ref(null)

const form = reactive({ id: null, roomNo: '', floorId: null, roomTypeId: null, roomStatusId: 1, roomStatusName: '空房', remark: '' })
const statusForm = reactive({ roomStatusId: null })
const formRules = {
  roomNo: [{ required: true, message: '请输入房号', trigger: 'blur' }],
  floorId: [{ required: true, message: '请选择楼层', trigger: 'change' }],
  roomTypeId: [{ required: true, message: '请选择房型', trigger: 'change' }]
}

function roomStatusTag(statusId) {
  const map = { 1: 'success', 2: 'warning', 3: 'primary', 4: 'info', 5: 'danger' }
  return map[statusId] || 'info'
}

async function loadList() {
  const res = await getRoomList(query)
  list.value = res.list || []
}

async function loadDict() {
  const [f, r, s] = await Promise.all([getFloorList(), getRoomTypeList(), getRoomStatusList()])
  floors.value = f || []
  roomTypes.value = (r?.list || r) || []
  roomStatusList.value = s || []
}

function openEdit(row) {
  if (row) {
    Object.assign(form, { id: row.id, roomNo: row.roomNo, floorId: row.floorId, roomTypeId: row.roomTypeId, roomStatusId: row.roomStatusId, roomStatusName: row.roomStatusName, remark: row.remark })
  } else {
    resetForm()
  }
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, { id: null, roomNo: '', floorId: null, roomTypeId: null, roomStatusId: 1, roomStatusName: '空房', remark: '' })
  formRef.value?.resetFields()
}

async function submitForm() {
  await formRef.value?.validate().catch(() => {})
  submitLoading.value = true
  try {
    await saveRoom(form)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadList()
  } catch (e) {
    ElMessage.error(e?.message || '保存失败')
  } finally {
    submitLoading.value = false
  }
}

function openStatus(row) {
  currentRoom.value = row
  statusForm.roomStatusId = row.roomStatusId
  statusDialogVisible.value = true
}

async function submitStatus() {
  const s = roomStatusList.value.find(x => x.id === statusForm.roomStatusId)
  await updateRoomStatus(currentRoom.value.id, statusForm.roomStatusId)
  ElMessage.success('状态已更新')
  statusDialogVisible.value = false
  loadList()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除该房间吗？', '提示', { type: 'warning' })
  await deleteRoom(row.id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(() => {
  loadDict()
  loadList()
})
</script>

<style scoped>
.page { padding: 0; }
.toolbar { margin-bottom: 12px; display: flex; gap: 8px; flex-wrap: wrap; }
</style>