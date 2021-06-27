<template>
  <!-- note: 其中 default-active 和标签页的活动页 editableTabsValue 同步 -->
  <el-menu
    :default-active="this.$store.state.menus.editableTabsValue"
    class="el-menu-vertical-demo"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b">
    <router-link to="/index">
      <el-menu-item index="Index" @click="showTab({name: 'Index', title: '首页'})">
        <template slot="title">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </template>
      </el-menu-item>
    </router-link>
    <!-- note: 一级目录 -->
    <el-submenu :index="menu.name" v-for="menu in menuList" :key="menu.name">
      <template slot="title">
        <i :class="menu.icon"></i>
        <span>{{ menu.title }}</span>
      </template>
      <!-- note: 二级目录 -->
      <router-link :to="item.path" v-for="item in menu.children" :key="item.name">
        <el-menu-item :index="item.name" @click="showTab(item)">
          <i :class="item.icon"></i>
          <span slot="title">{{ item.title }}</span>
        </el-menu-item>
      </router-link>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: 'SideMenu',
  data() {
    return {

    }
  },
  // note: 数据放 computed 里面可以实时更新
  computed: {
    // note: 通过 menuList 根据不同的用户权限，显示不同的 menu 导航目录，需从后端获取，mock.js 中有 JSON 数据格式示例
    // note: 路由也需动态绑定，所以 menuList 数据在路由中获取：router.beforeEach()，并存入 store 中
    menuList: {
      get() {
        return this.$store.state.menus.menuList
      }
    }
  },
  methods: {
    // 展示 Tab 标签页
    showTab(item) {
      this.$store.commit("addTab", item);
    }
  },

}
</script>

<style scoped>
  .el-menu-vertical-demo {
    height: 100%;    /* note: 左侧导航占满整个 aside 部分 */
  }
  /* note: 去掉 a 标签下划线 */
  a {
    text-decoration: none;
  }
</style>