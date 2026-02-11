// 普通客人（非会员）接口
import request from './request'

export function getGuestList(params = {}) {
  return request({
    url: '/api/guest/list',
    method: 'get',
    params
  })
}

export function getGuestById(id) {
  return request({
    url: `/api/guest/${id}`,
    method: 'get'
  })
}

export function saveGuest(data) {
  return request({
    url: '/api/guest/save',
    method: 'post',
    data
  })
}

export function deleteGuest(id) {
  return request({
    url: `/api/guest/${id}`,
    method: 'delete'
  })
}

export function getGuestByPhone(phone) {
  return request({
    url: `/api/guest/by-phone/${phone}`,
    method: 'get'
  })
}

export function getGuestByIdNo(idNo) {
  return request({
    url: `/api/guest/by-id-no/${idNo}`,
    method: 'get'
  })
}
