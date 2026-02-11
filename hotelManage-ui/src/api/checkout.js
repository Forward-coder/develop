import request from './request'

// 获取待退房客人列表
export function getPendingCheckoutList() {
  return request({
    url: '/api/checkout/pending',
    method: 'get'
  })
}

// 获取退房预览信息
export function getCheckoutPreview(checkInId) {
  return request({
    url: `/api/checkout/${checkInId}/preview`,
    method: 'get'
  })
}

// 办理退房结账
export function doCheckout(data) {
  return request({
    url: '/api/checkout/checkout',
    method: 'post',
    data
  })
}