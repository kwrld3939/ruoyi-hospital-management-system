import request from '@/utils/request'

// 查询医生工作台
export function getDoctorWorkbench(query) {
  return request({
    url: '/hospital/workbench/doctor',
    method: 'get',
    params: query
  })
}
