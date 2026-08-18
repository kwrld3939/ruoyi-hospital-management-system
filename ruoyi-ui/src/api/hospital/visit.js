import request from '@/utils/request'

// 查询就诊记录列表
export function listVisit(query) {
  return request({
    url: '/hospital/visit/list',
    method: 'get',
    params: query
  })
}

// 查询就诊记录详细
export function getVisit(visitId) {
  return request({
    url: '/hospital/visit/' + visitId,
    method: 'get'
  })
}

// 新增就诊记录
export function addVisit(data) {
  return request({
    url: '/hospital/visit',
    method: 'post',
    data: data
  })
}

// 修改就诊记录
export function updateVisit(data) {
  return request({
    url: '/hospital/visit',
    method: 'put',
    data: data
  })
}

// 删除就诊记录
export function delVisit(visitId) {
  return request({
    url: '/hospital/visit/' + visitId,
    method: 'delete'
  })
}
