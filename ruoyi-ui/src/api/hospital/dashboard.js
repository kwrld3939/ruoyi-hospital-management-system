import request from '@/utils/request'

// 查询医院首页看板
export function getHospitalDashboard() {
  return request({
    url: '/hospital/dashboard',
    method: 'get'
  })
}
