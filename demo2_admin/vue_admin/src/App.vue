<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
  watch: {
    // 打开或刷新页面时，查看路由情况，根据路由信息打开标签页
    $route(to, from) {
      if(to.path != '/login') {
        let obj = {
          name: to.name,
          title: to.meta.title
        }

        this.$store.commit("addTab", obj)
      }
    }
  }
}
</script>

<style>
/* note: 设置主体样式 */
html,body,#app {
  font-family: "Helvetica Neue", "Hiragino Sans GB", "WenQuanYi Micro Hei", "Microsoft Yahei", sans-serif;
  height: 100%;
  padding: 0;
  margin: 0;
  font-size: 15px;
}
</style>
