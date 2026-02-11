// 系统用户（员工账号）接口 mock

let users = [
  { id: 1, username: 'admin', realName: '系统管理员', phone: '', roleIds: [1], roleNames: '管理员', isEnabled: 1 },
  { id: 2, username: 'reception1', realName: '前台小李', phone: '13700001111', roleIds: [2], roleNames: '前台', isEnabled: 1 }
]
let idSeq = 3

export function getSystemUserList(params = {}) {
  let list = [...users]
  if (params.username) list = list.filter(u => u.username.includes(params.username))
  if (params.realName) list = list.filter(u => (u.realName || '').includes(params.realName))
  return Promise.resolve({ list, total: list.length })
}

export function getSystemUserById(id) {
  const row = users.find(u => u.id === Number(id))
  return Promise.resolve(row ? { ...row } : null)
}

export function saveSystemUser(data) {
  if (data.id) {
    const idx = users.findIndex(u => u.id === data.id)
    if (idx >= 0) {
      users[idx] = { ...users[idx], ...data }
      return Promise.resolve(users[idx])
    }
  } else {
    const newRow = { id: idSeq++, ...data, isEnabled: data.isEnabled ?? 1 }
    users.push(newRow)
    return Promise.resolve(newRow)
  }
  return Promise.reject(new Error('保存失败'))
}

export function deleteSystemUser(id) {
  users = users.filter(u => u.id !== Number(id))
  return Promise.resolve()
}
