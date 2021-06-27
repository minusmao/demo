import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

/* note: 1、引入其它模块 */
import Element from 'element-ui'    // note: 前端 UI 框架，执行 "npm install element-ui --save" 安装
import 'element-ui/lib/theme-chalk/index.css'
// import axios from 'axios'        // note: 前端 HTTP 框架，执行 "npm install axios --save" 安装
import request from '@/axios'       // note: 导入自己创建好的 axios 对象，在自己创建的 axios.js 文件中

import global from './globalFun'    // 全局方法文件

/* note: 2、加载到 Vue 中（两种方式的区别：https://www.e-learn.cn/topic/3511857） */
Vue.use(Element)                    // note: 直接引入的方式
// Vue.prototype.$axios = axios     // note: 实例上挂载属性/方法的方式
Vue.prototype.$request = request    // 挂载 request 属性（axios 对象）

/* note: 阻止显示生产模式的消息，详情：https://www.cnblogs.com/itgezhu/p/11949621.html */
Vue.config.productionTip = false

/* note: 引入 mock 模块，关闭则注释该行 */
require("./mock")                   // note: 随机数据框架，可拦截前端 ajax 请求，模拟后端响应，执行 "npm install mockjs --save-dev" 安装 

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
