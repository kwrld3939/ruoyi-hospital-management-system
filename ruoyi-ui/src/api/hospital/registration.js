import request from '@/utils/request'

// 查询预约挂号列表
export function listRegistration(query) {
  return request({
    url: '/hospital/registration/list',
    method: 'get',
    params: query
  })
}

// 查询预约挂号详细
export function getRegistration(registrationId) {
  return request({
    url: '/hospital/registration/' + registrationId,
    method: 'get'
  })
}

// 新增预约挂号
export function addRegistration(data) {
  return request({
    url: '/hospital/registration',
    method: 'post',
    data: data
  })
}

// 修改预约挂号
export function updateRegistration(data) {
  return request({
    url: '/hospital/registration',
    method: 'put',
    data: data
  })
}

// 取消预约挂号
export function cancelRegistration(data) {
  return request({
    url: '/hospital/registration/cancel',
    method: 'put',
    data: data
  })
}

// 删除预约挂号
export function delRegistration(registrationId) {
  return request({
    url: '/hospital/registration/' + registrationId,
    method: 'delete'
  })
}
