/**
 * 文件名：axios.js
 * 描述：统一拦截 axios 响应，处理错误
 * 作者：TechRice
 * 时间：2021-6-20
 */

// note: 引入模块
import axios from "axios";
import router from "@/router";
import Element from "element-ui";    // note: 用于错误弹窗

// note: 统一请求后端的 URL
axios.defaults.baseURL = "http://localhost:8088"    // note: 如果还没有搭建后端，端口不同，使用 mock 时有跨域问题（后端可以解决），可以先注释掉

// note: 定义一个 axios 对象
const request = axios.create({
  // note: 超时时间
  timeout: 5000,
  // note: HTTP 报文头参数
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

// note: 拦截 request 请求
request.interceptors.request.use(config => {
  // note：为表明自己的身份，每次请求都带上 token 数据，即登陆时从后台得到的 jwt
  config.headers['Authorization'] = localStorage.getItem('token')    // note: 从本地 localStorage 获取
  return config
})

// note: 拦截 response 响应
/*
  后端响应 response.data 的结构如下；
    {
      "code": 200,
      "msg": "响应信息",
      "data": {
        "key1": "value1",
        "key2": "value2",
        ...
      }
    }
*/
request.interceptors.response.use(
  response => {
    // note: 得到响应报文的数据部分
    let res = response.data

    // note: 判断状态码
    if (res.code === 200) {
      // note: 200 成功
      return response
    } else {
      // note: 显示错误信息，当 res.msg 为空时显示'系统异常'
      Element.Message.error(res.msg ? res.msg : '系统异常')
      // note: 拒绝请求
      return Promise.reject(response.data.msg)
    }
  },
  // note: 响应异常时
  error => {
    console.log(error)
    // note: 判断 data 是否有数据
    if (error.response.data) {
      error.message = error.response.data.msg
    }
    // note: 判断 status 状态码，若为 401 说明权限不够，重新登陆
    if (error.response.status === 401) {
      // note: 清除本地的数据
      localStorage.clear()
      sessionStorage.clear()
      this.$store.commit("resetState")
      // 跳转到登陆界面
      router.push("/login")
    }
    // note: 显示错误信息
    Element.Message.error(error.message, { duration: 3 * 1000 })   // note: 弹窗时间 3 秒

    return Promise.reject(error)
  }
)

// note: 导出
export default request