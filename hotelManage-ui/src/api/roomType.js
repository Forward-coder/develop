import request from './request'

// 获取房型列表
 export function getRoomTypeList(params = {}) {
  return request({
    url: '/api/room-type/list',
    method: 'get',
    params
  }).then(res => {
    return {
      list: res.data || [],
      total: (res.data || []).length
    }
  })
}

// 获取房型详情
export function getRoomTypeById(id) {
  return request({
    url: `/api/room-type/${id}`,
    method: 'get'
  }).then(res => res.data)
}

// 保存房型（新增或更新）
export function saveRoomType(data) {
  if (data.id) {
    return request({
      url: `/api/room-type/${data.id}`,
      method: 'put',
      data
    }).then(res => res.data)
  } else {
    return request({
      url: '/api/room-type/save',
      method: 'post',
      data
    }).then(res => res.data)
  }
}

// 删除房型
export function deleteRoomType(id) {
  return request({
    url: `/api/room-type/${id}`,
    method: 'delete'
  })
}
