// 会员管理接口
import request from './request'

export function getMemberList(params = {}) {
  return request({
    url: '/api/member/list',
    method: 'get',
    params
  })
}

export function getMemberById(id) {
  return request({
    url: `/api/member/${id}`,
    method: 'get'
  })
}

export function saveMember(data) {
  return request({
    url: '/api/member/save',
    method: 'post',
    data
  })
}

export function deleteMember(id) {
  return request({
    url: `/api/member/${id}`,
    method: 'delete'
  })
}

export function getMemberByPhone(phone) {
  return request({
    url: `/api/member/by-phone/${phone}`,
    method: 'get'
  })
}

export function getMemberByMemberNo(memberNo) {
  return request({
    url: `/api/member/by-member-no/${memberNo}`,
    method: 'get'
  })
}

export function getMemberLevelList() {
  return request({
    url: '/api/member/levels',
    method: 'get'
  })
}

export function adjustPoints(memberId, points, remark) {
  return request({
    url: `/api/member/${memberId}/adjust-points`,
    method: 'post',
    params: { points, remark }
  })
}

export function saveMemberLevel(data) {
  return request({
    url: '/api/member/level/save',
    method: 'post',
    data
  })
}

export function toggleMemberLevelStatus(id, isEnabled) {
  return request({
    url: `/api/member/level/${id}/toggle-status`,
    method: 'post',
    params: { isEnabled }
  })
}

// 统计报表相关接口
export function getMemberStatistics() {
  return request({
    url: '/api/member/statistics/overview',
    method: 'get'
  })
}

export function getLevelDistribution() {
  return request({
    url: '/api/member/statistics/level-distribution',
    method: 'get'
  })
}

export function getMonthlyGrowth() {
  return request({
    url: '/api/member/statistics/monthly-growth',
    method: 'get'
  })
}

export function getPointsRanking(limit = 10) {
  return request({
    url: '/api/member/statistics/points-ranking',
    method: 'get',
    params: { limit }
  })
}