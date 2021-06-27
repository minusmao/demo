import Vue from 'vue'
import VueRouter from 'vue-router'

/* note: 视图 view 预加载 */
import Home from '@/views/Home.vue'
import Index from '@/views/Index.vue'
import Login from '@/views/Login.vue'

// note: 导入
import request from '@/axios'
import store from '@/store'

Vue.use(VueRouter)

/* note: 路由配置 */
const routes = [
  {
    path: '/',    // 父组件：Home 主页
    name: 'Home',
    component: Home,
    redirect: '/index',
    children: [
      {
        path: '/index',    // 子组件：index 主页
        name: 'Index',
        component: Index,
      },
      {
        path: '/userCenter',
        name: 'UserCenter',
        component: () => import('@/views/UserCenter.vue'),
        meta: {
          title: '个人中心'
        }
      },
      // {
      //   path: '/sys/users',    // 子组件：用户管理页面
      //   name: 'SysUsers',
      //   component: () => import('@/views/sys/User.vue')
      // },
      // {
      //   path: '/sys/roles',    // 子组件：角色管理页面
      //   name: 'SysRoles',
      //   component: () => import('@/views/sys/Role.vue')
      // },
      // {
      //   path: '/sys/menus',    // 子组件：菜单管理页面
      //   name: 'SysMenus',
      //   component: () => import('@/views/sys/Menu.vue')
      // }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// note: 动态路由绑定，并获取目录 MenuList，在路由之前执行
router.beforeEach((to, from, next) => {
  let hasRoute = store.state.menus.hasRoutes    // 标志位，如果已经获取了 MenuList 就不需要获取了
  let token = localStorage.getItem("token")

  if (to.path == '/login') {
    // 登陆路径，直接进入
    next()

  } else if (!token) {
    // 没有 token 请登录
    next({ path: '/login' })

  } else if (token && !hasRoute) {
    // to and from are both route objects. must call `next`.
    request.get("/sys/menu/nav").then(response => {
      // 拿到 menuList 并存入 store 的 menus 中
      store.commit("setMenuList", response.data.data.nav)
      // 拿到 authorities 并存入 store 的 menus 中
      store.commit("setPermList", response.data.data.authorities)

      // note: 动态绑定路由
      let newRoutes = router.options.routes    // 获得现有的路由副本
      // 一级路由
      response.data.data.nav.forEach(menu => {
        // 如果有 children 子路由
        if (menu.children) {
          // 二级路由
          menu.children.forEach(item => {
            // item 数据对象 ===> route 路由对象
            let route = menuToRoute(item)
            // 添加到 newRoutes 路由中
            if (route) {
              newRoutes[0].children.push(route)
            }
          })
        }
      })
      // 使用改好的副本作为路由
      router.addRoutes(newRoutes)
      // 更改标志位
      hasRoute = true
			store.commit("changeRouteStatus", hasRoute)
    })
  }
  next()
})


// 导航转成路由（item 数据对象 ===> route 路由对象）
const menuToRoute = (menu) => {

	if (!menu.component) {
		return null
	}

  let route = {
    path: menu.path,
		name: menu.name,
		component: () => import('../views/' + menu.component +'.vue'),
		meta: {
			icon: menu.icon,
			title: menu.title
		}
	}
	// route.component = () => import('../views/' + menu.component +'.vue')

	return route
}

export default router
