import Vue from 'vue'
import Vuex from 'vuex'

// note: 导入自己定义的全局数据
import menus from "./modules/menus"

Vue.use(Vuex)

/* note: 状态管理，存储全局数据的地方 */
export default new Vuex.Store({
  state: {
    /* note: 全局数据 */
    token: ''    // note:   存储登陆时，后端返回的 jwt
  },
  mutations: {
    /* note: 操作数据 */
    // 保存 token 数据 jwt
    SET_TOKEN: (state, token) => {
      state.token = token                     // note: 存入 store 中
      localStorage.setItem("token", token)    // note: 存入 localStorage 中
    }
  },
  actions: {
  },
  modules: {
    menus
  }
})
