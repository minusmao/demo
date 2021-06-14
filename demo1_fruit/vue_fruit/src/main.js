import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'

import * as echarts from 'echarts'    // 引入echarts

Vue.prototype.$echarts = echarts    // 将echarts注册到Vue组件的原型对象中去

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
