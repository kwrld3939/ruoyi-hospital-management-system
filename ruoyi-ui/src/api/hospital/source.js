import request from '@/utils/request'

// 查询号源列表
export function listSource(query) {
  return request({
    url: '/hospital/source/list',
    method: 'get',
    params: query
  })
}

// 查询号源详细
export function getSource(sourceId) {
  return request({
    url: '/hospital/source/' + sourceId,
    method: 'get'
  })
}

// 新增号源
export function addSource(data) {
  return request({
    url: '/hospital/source',
    method: 'post',
    data: data
  })
}

// 修改号源
export function updateSource(data) {
  return request({
    url: '/hospital/source',
    method: 'put',
    data: data
  })
}

// 删除号源
export function delSource(sourceId) {
  return request({
    url: '/hospital/source/' + sourceId,
    method: 'delete'
  })
}
