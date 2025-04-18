import axios from 'axios'
import { BASE_URL } from './config/apiConfig'
const myAxios = axios.create({
  baseURL: BASE_URL,
  // `withCredentials` 表示跨域请求时是否需要使用凭证
  withCredentials: true,
  timeout: 60000, // default
})

// 添加请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// 添加响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error)
  },
)

export default myAxios
