import request from './request'

// 获取楼层列表
export function getFloorList() {
  return request({
    url: '/api/floor/list',
    method: 'get'
  }).then(res => res.data || [])
}

// 获取房间列表
export function getRoomList(params = {}) {
  return request({
    url: '/api/room/list',
    method: 'get',
    params
  }).then(res => {
    return {
      list: res.data || [],
      total: (res.data || []).length
    }
  })
}

// 获取房间详情
export function getRoomById(id) {
  return request({
    url: `/api/room/${id}`,
    method: 'get'
  }).then(res => res.data)
}

// 保存房间（新增或更新）
export function saveRoom(data) {
  if (data.id) {
    return request({
      url: `/api/room/${data.id}`,
      method: 'put',
      data
    }).then(res => res.data)
  } else {
    return request({
      url: '/api/room/save',
      method: 'post',
      data
    }).then(res => res.data)
  }
}

// 删除房间
export function deleteRoom(id) {
  return request({
    url: `/api/room/${id}`,
    method: 'delete'
  })
}

// 更新房间状态
export function updateRoomStatus(roomId, roomStatusId) {
  return request({
    url: `/api/room/${roomId}/status`,
    method: 'patch',
    params: { statusId: roomStatusId }
  }).then(res => res.data)
}