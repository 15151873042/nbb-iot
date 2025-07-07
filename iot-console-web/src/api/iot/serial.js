import request from '@/utils/request'

// 查询岗位列表
export function listPageSerial(query) {
  return request({
    url: '/iot/serial/page',
    method: 'get',
    params: query
  })
}
