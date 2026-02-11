import request from './request'

// 获取床型字典
export function getBedTypes() {
  return request({
    url: '/api/dict/bed-types',
    method: 'get'
  }).then(res => res.data || [])
}

// 获取房间状态字典
export function getRoomStatusList() {
  return request({
    url: '/api/dict/room-status',
    method: 'get'
  }).then(res => res.data || [])
}

// 获取预订状态字典
export function getReservationStatusList() {
  return request({
    url: '/api/dict/reservation-status',
    method: 'get'
  }).then(res => res.data || [])
}

// 获取预订渠道字典
export function getReservationChannelList() {
  return request({
    url: '/api/dict/reservation-channel',
    method: 'get'
  }).then(res => res.data || [])
}

// 获取证件类型字典
export function getIdTypeList() {
  return request({
    url: '/api/dict/id-type',
    method: 'get'
  }).then(res => res.data || [])
}

// 获取支付方式字典
export function getPaymentMethodList() {
  return request({
    url: '/api/dict/payment-method',
    method: 'get'
  }).then(res => res.data || [])
}