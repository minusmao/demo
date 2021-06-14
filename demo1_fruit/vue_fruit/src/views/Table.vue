<!-- 使用 Element UI 框架 -->
<template>
  <div>
    <el-table
      :data="tableData"
      height="400"
      border
      style="width: 100%">
      <!-- 设置列名与后端发过来的 JSON 数据对应，客户端返回的是多个 JSON 对象组成的数组 -->
      <el-table-column
        fixed
        prop="id"
        label="水果ID"
        width="160">
      </el-table-column>
      <el-table-column
        prop="name"
        label="水果名称"
        width="160">
      </el-table-column>
      <el-table-column
        prop="sale"
        label="水果销量"
        width="160">
      </el-table-column>
      <el-table-column
        prop="iconUrl"
        label="图片"
        width="160">
        <!-- 显示图片，而不是显示图片的 URL -->
        <template slot-scope="scope">
            <img :src="scope.row.iconUrl" style="height: 70px"/>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="handleClickDelete(scope.row)" type="text" size="small">删除</el-button>
          <el-button @click="handleClickEdit(scope.row)" type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="primary" @click="handleClickAdd()" style="margin-top: 20px">增加水果</el-button>
  </div>
</template>

<script>
  export default {
    methods: {
      handleClickAdd() {
        // 路由跳转到增加页面
        this.$router.push('/add');
      },
      handleClickEdit(row) {
        // 路由跳转到编辑页面
        this.$router.push('/edit?id=' + row.id);
      },
      handleClickDelete(row) {
        // 使用 Element UI 的删除弹框
        this.$confirm('是否确定要删除'+ row.name +'?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 根据当前行的 id 属性调用删除接口，这里使用箭头形式注册回调函数
          axios.delete('http://localhost:8088/fruit/delete/' + row.id).then((response) => {
            // 服务端返回 bool 值
            if(response.data) {
              // 刷新页面
              location.reload();
              // 提示删除成功
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            } else {
              // 提示删除失败
              this.$message({
                type: 'success',
                message: '删除失败!'
              });
            }
          });
        }).catch(() => {
          //  提示取消删除
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
      }
    },
    // 通过 axios 获取数据再填入
    created() {
        // 得到全局 this 对象，避免和回调函数中自己的 this 冲突
        let _this = this;    // 如果回调函数用箭头方式就不用考虑这个问题，形式如下：(response) => {...}
        // 发起请求
        axios.get('http://localhost:8088/fruit/list').then(function (response) {
            // 得到结果
            _this.tableData = response.data;
        });
    },
    data() {
      return {
        tableData: null
      }
    }
  }
</script>