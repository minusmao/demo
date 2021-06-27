<template>
  <!-- note: Element 的 Container 布局容器 -->
  <el-container>

    <!-- note: 左边 aside 部分 -->
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>

    <el-container>
      <!-- note: 右边 header 部分 -->
      <el-header>
        <strong>AdminDemo后台管理系统</strong>
        <!-- note: 用户部分 -->
        <div class="header-avatar">
          <el-avatar size="small" :src="userInfo.avatar"></el-avatar>
          <el-dropdown>
            <span class="el-dropdown-link">
              {{userInfo.username}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <router-link to="/userCenter">
                <el-dropdown-item @click.native="showTab({name: 'UserCenter', title: '个人中心'})">个人中心</el-dropdown-item>
              </router-link>
              <el-dropdown-item @click.native="logout()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link href="https://github.com/TechRice" target="_blank">网站</el-link>
        </div>
      </el-header>

      <!-- note: 右边 main 部分 -->
      <el-main>
        <!-- note: 标签页 -->
        <Tabs></Tabs>
        <!-- note: 显示子级路由 children 中的组件 -->
        <div style="margin: 0 15px">
          <router-view />
        </div>
      </el-main>
    </el-container>

  </el-container>
</template>

<script>
/* note: 导入组件 */
import SideMenu from './inc/SideMenu'    // 侧边目录 SideMenu 
import Tabs from './inc/Tabs.vue'        // 标签页

export default {
  name: 'Home',
  // note: 使用的组件
  components: {
    SideMenu,
    Tabs
  },
  data() {
    return {
      // note: 用户信息数据，从后端获取
      userInfo: {
        id: '',
        username: '',
        avatar: ''
      }
    }
  },
  // note: 视图创建时调用
  created() {
    this.getUserInfo();
  },
  methods: {
    // note: 请求后端获得用户信息
    getUserInfo() {
      this.$request.get('/sys/userInfo').then(response => {
        this.userInfo = response.data.data
      })
    },
    // note: 退出登陆
    logout() {
      this.$request.post("/logout").then(response => {
        // note: 清除本地的数据
        localStorage.clear()
        sessionStorage.clear()
        this.$store.commit("resetState")
        // note: 跳转到登陆页面
        this.$router.push("/login")
      })
    },
    // 展示 Tab 标签页
    showTab(item) {
      this.$store.commit("addTab", item);
    }
  }
}
</script>

<style scoped>
  /* note: 总体样式 */
  .el-container {
    padding: 0;
    margin: 0;
    height: 100%;
  }
  /* note: header 部分的样式 */
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  /* note: 用户部分的样式 */
  .header-avatar{
    float: right;
    width: 180px;
    display: flex;
		justify-content: space-around;
		align-items: center;
  }
  .el-dropdown-link {
    cursor: pointer;    /* note: 设置该组件的鼠标样式为手 */
  }
  /* note: aside 部分的样式 */
  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    line-height: 200px;
  }
  /* note: main部分的样式 */
  .el-main {
    color: #333;
    padding: 0;
  }
  /* note: 去掉 a 标签下划线 */
  a {
    text-decoration: none;
  }
</style>