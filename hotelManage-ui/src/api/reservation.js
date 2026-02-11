import request from './request'

// 获取预订列表
export function getReservationList(params = {}) {
  return request({
    url: '/api/reservation/list',
    method: 'get',
    params
  }).then(res => {
    return {
      list: res.data || [],
      total: (res.data || []).length
    }
  })
}

// 获取预订详情
export function getReservationById(id) {
  return request({
    url: `/api/reservation/${id}`,
    method: 'get'
  }).then(res => res.data)
}

// 保存预订（新增或更新）
export function saveReservation(data) {
  if (data.id) {
    return request({
      url: `/api/reservation/${data.id}`,
      method: 'put',
      data
    }).then(res => res.data)
  } else {
    return request({
      url: '/api/reservation/save',
      method: 'post',
      data
    }).then(res => res.data)
  }
}

// 取消预订
export function cancelReservation(id) {
  return request({
    url: `/api/reservation/${id}/status`,
    method: 'patch',
    params: { statusId: 3 }
  }).then(res => res.data)
}

// 删除预订
export function deleteReservation(id) {
  return request({
    url: `/api/reservation/${id}`,
    method: 'delete'
  })
}
