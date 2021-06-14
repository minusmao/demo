import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

/* 笔记：导入新建 views 中的视图 */
import Pie from '../views/Pie.vue'
import Bar from '../views/Bar.vue'
import Table from '../views/Table.vue'
import Edit from '../views/Edit.vue'
import Add from '../views/Add.vue'

Vue.use(VueRouter)

/* 笔记：路由配置 */
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/pie',
    component: Pie
  },
  {
    path: '/bar',
    component: Bar
  },
  {
    path: '/table',
    component: Table
  },
  {
    path: '/edit',
    component: Edit
  },
  {
    path: '/add',
    component: Add
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
