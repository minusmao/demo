<template>
  <el-tabs v-model="editableTabsValue" type="card" closable @tab-remove="removeTab" @tab-click="clickTab">
    <el-tab-pane
      v-for="item in editableTabs"
      :key="item.name"
      :label="item.title"
      :name="item.name"
    >
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  export default {
    data() {
      return {

      }
    },
    // note: 根据状态管理器的数据实时更新以下数据
    computed: {
      // 默认打开的 Tab
      editableTabs: {
        get() {
          return this.$store.state.menus.editableTabs
        },
        set(val) {
          this.$store.state.menus.editableTabs = val;
        }
      },
      // 所有 Tab 的数据
      editableTabsValue: {
        get() {
          return this.$store.state.menus.editableTabsValue
        },
        set(val) {
          this.$store.state.menus.editableTabsValue = val;
        }
      }
    },
    methods: {
      // 删除 Tab 标签页（添加的方法在 store/modules/menus.js 中）
      removeTab(targetName) {
        let tabs = this.editableTabs;
        let activeName = this.editableTabsValue;
        // note: 如果只剩一个标签，不能删除
        if (tabs.length <= 1) {
          return
        }
        // note: 判断如果删除的是当前激活的，寻找下一个激活的 Tab 标签页
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;                // 获得下一个要激活的 Tab 标签页
                this.$router.push({name: activeName});    // 跳转到该标签对应的路由
              }
            }
          });
        }
        
        this.editableTabsValue = activeName;    // 设置激活的 Tab 标签页
        this.editableTabs = tabs.filter(tab => tab.name !== targetName);
      },
      // 点击 Tab 标签页，其中 target 为被点击的 Tab 对象
      clickTab(target) {
        // 路由跳转（需避免重复跳转，即路由没变时，不跳转）
        if(this.$route.name != target.name) {
          this.$router.push({name: target.name})
        }
      }
    }
  }
</script>

<style scoped>

</style>