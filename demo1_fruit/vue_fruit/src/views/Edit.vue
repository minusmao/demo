<!-- 使用 Element UI 框架 -->
<template>
  <el-form ref="fruitUpdate" :model="fruit" :rules="rules" label-width="80px" class="demo-ruleForm" style="width: 100%">
    <el-form-item label="ID" prop="id">
      <!-- 数据双向绑定，且只读 -->
      <el-input v-model="fruit.id" readonly></el-input>
    </el-form-item>
    <!-- 笔记：prop 属性指定校验规则 -->
    <el-form-item label="名称" prop="name">
      <!-- 数据双向绑定 -->
      <el-input v-model="fruit.name"></el-input>
    </el-form-item>
    <el-form-item label="销量" prop="sale">
      <!-- 数据双向绑定 -->
      <el-input v-model.number="fruit.sale"></el-input>
    </el-form-item>
    <el-form-item label="图片" prop="iconUrl">
      <!-- 数据双向绑定 -->
      <el-input v-model="fruit.iconUrl"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit('fruitUpdate')">立即更新</el-button>
      <el-button>取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: "Edit",
    data() {
      return {
        // 数据对象
        fruit: {
          id: '',
          name: '',
          sale: '',
          iconUrl: ''
        },
        // 校验规则
        rules: {
          name:[
            { required: true, message: '请输入水果名称', trigger: 'blur' }
          ],
          sale:[
            { required: true, message: '请输入销量', trigger: 'blur' },
            { type: 'number', message: '销量必须为数字值'}
          ],
          iconUrl:[
            { required: true, message: '请输入图片链接', trigger: 'blur' },
            { min: 0, max: 255, message: '长度不得超出 255 个字符', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      // 提交表单时调用的方法
      onSubmit(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let _this = this;
            // 发送 PUT 请求，并带上 fruit 对象
            axios.put('http://localhost:8088/fruit/update', this.fruit).then(function (response) {
              // 后端返回 bool 值
              if(response.data){
                // 更新成功
                _this.$alert(_this.fruit.name + '更新成功！', '修改数据', {
                  confirmButtonText: '确定',
                  callback: action => {
                    //跳转到/table
                    _this.$router.push('/table')
                  }
                });
              } else {
                // 更新失败
                 _this.$alert(_this.fruit.name + '更新失败！', '修改数据', {
                  confirmButtonText: '确定'
                });
              }
            })
          }
        });
      }
    },
    // 创建时调用，请求后端的数据并赋值给 fruit 对象
    created() {
      // 得到传入的 id 值
      let id = this.$route.query.id;
      // 发起请求
      axios.get('http://localhost:8088/fruit/find/' + id).then((response) => {
        // 将服务端返回的 JSON 对象赋值到 fruit 对象上
        this.fruit = response.data;
      });
    }
  }
</script>