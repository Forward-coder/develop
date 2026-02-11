// 入住/退房接口（真实API）
import request from './request'

export function getCheckInList(params = {}) {
  return request({
    url: '/api/check-in/list',
    method: 'get',
    params
  })
}

export function getCheckInById(id) {
  return request({
    url: `/api/check-in/${id}`,
    method: 'get'
  })
}

export function doCheckIn(data) {
  return request({
    url: '/api/check-in/check-in',
    method: 'post',
    data
  })
}

export function doCheckOut(id) {
  return request({
    url: `/api/check-in/${id}/check-out`,
    method: 'post'
  })
}

export function extendStay(id, newCheckOutDate) {
  return request({
    url: `/api/check-in/${id}/extend`,
    method: 'post',
    data: newCheckOutDate
  })
}

export function getStayingList() {
  return request({
    url: '/api/check-in/staying',
    method: 'get'
  })
}
