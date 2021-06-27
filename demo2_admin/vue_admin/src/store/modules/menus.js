import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

// note: 路由和视图共享的目录信息 menuList
export default {
	state: {
		menuList: [],    // 菜单列表信息
    permList: [],    // 权限信息

    hasRoutes: false,    // 是否已请求 menuList、permList 的标志

    // 默认打开的 Tab 标签页
    editableTabsValue: 'Index',
    // 所有 Tab 标签页的数据（其余从 menuList 中解析）
    editableTabs: [{
      title: '首页',
      name: 'Index'
    }]
	},
	mutations: {
		setMenuList(state, menus) {
			state.menuList = menus
		},
		setPermList(state, perms) {
			state.permList = perms
    },
    changeRouteStatus(state, hasRoutes) {
			state.hasRoutes = hasRoutes
    },
    // 添加 Tab 标签页
    addTab(state, tab) {
      // 判断是否已存在
      let index = state.editableTabs.findIndex(e => e.name === tab.name)    // 返回name相同的序号，没有则为 -1
      // 没有则添加
      if (index == -1) {
        state.editableTabs.push({
          title: tab.title,
          name: tab.name,
        });
      }
      // 激活
      state.editableTabsValue = tab.name;
    },
    // 重置数据，退出登陆时用
    resetState: (state) => {
      state.menuList = []
      state.permList = []
      state.hasRoutes = false
      state.editableTabsValue = 'Index'
      state.editableTabs = [{
        title: '首页',
        name: 'Index'
      }]
    }
  },
	actions: {
	}

}