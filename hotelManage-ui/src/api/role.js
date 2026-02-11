// 角色管理接口 mock

let roles = [
  { id: 1, code: 'admin', name: '管理员', isEnabled: 1 },
  { id: 2, code: 'reception', name: '前台', isEnabled: 1 },
  { id: 3, code: 'finance', name: '财务', isEnabled: 1 }
]
let idSeq = 4

export function getRoleList() {
  return Promise.resolve({ list: [...roles], total: roles.length })
}

export function getRoleById(id) {
  const row = roles.find(r => r.id === Number(id))
  return Promise.resolve(row ? { ...row } : null)
}

export function saveRole(data) {
  if (data.id) {
    const idx = roles.findIndex(r => r.id === data.id)
    if (idx >= 0) {
      roles[idx] = { ...roles[idx], ...data }
      return Promise.resolve(roles[idx])
    }
  } else {
    const newRow = { id: idSeq++, ...data, isEnabled: data.isEnabled ?? 1 }
    roles.push(newRow)
    return Promise.resolve(newRow)
  }
  return Promise.reject(new Error('保存失败'))
}

export function deleteRole(id) {
  roles = roles.filter(r => r.id !== Number(id))
  return Promise.resolve()
}
