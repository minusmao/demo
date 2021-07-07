<template>
  <!-- note: Element 的 Layout 布局：对齐方式 -->
  <el-row type="flex" class="row-bg" justify="center">

    <!-- note: 左边部分 -->
    <el-col :xl="6" :lg="7">
      <h2>欢迎来到 AdminDemo 后台管理系统</h2>
      <el-image :src="require('@/assets/logo.png')" style="height: 200px; width: 200px"></el-image>
      <p>源码地址：https://github.com/TechRice/Demo</p>
    </el-col>

    <!-- note: 垂直分割线 -->
    <el-col :span="1">
      <el-divider direction="vertical"></el-divider>
    </el-col>

    <!-- note: 右边部分 -->
    <el-col :xl="6" :lg="7">
      <!-- note: 带验证功能的表单 -->
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px">
        <el-form-item label="用户名" prop="username" style="width: 380px">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" style="width: 380px">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" style="width: 380px">
          <el-input v-model="loginForm.code" style="width: 150px; float: left"></el-input>
          <el-image class="captchaImg" :src="captchaImg" @click="getCaptcha"></el-image>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')">立即登陆</el-button>
          <el-button @click="resetForm('loginForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>

  </el-row>
</template>

<script>
import qs from 'qs'    // 引入 qs 用于登陆时提交 form 表单

export default {
  name: 'Login',
  data() {
    return {
      // note: 表单数据
      loginForm: {
        username: '',    // note: 用户名
        password: '',    // note: 密码
        code: '',        // nore: 验证码
        token: ''        // note: 随机码（从后端获得，后端 Redis 中存了 【token : code】 的键值对）
      },
      // note: 输入校验
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          {min: 5, max: 5, message: '验证码为5个字符', trigger: 'blur'}
        ]
      },
      // note: 验证码图片（base64编码，从后端获得）
      captchaImg: ''
    };
  },
  methods: {
    // note: 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          /* note: 前面为固定写法，在这里请求后台数据 */
          // 注意：后台用了 Spring Security，默认登陆使用 form 表单形式提交，所以这里不要使用 JSON 格式提交
          // this.$request.post('/login', this.loginForm).then(response => {
          this.$request.post('/login?' + qs.stringify(this.loginForm)).then(response => {
            // note: 登陆成功，后端返回 jwt 数据
            const jwt = response.headers['authorization']
            // note: 将 jwt 存入 store 中
            this.$store.commit('SET_TOKEN', jwt)
            // note: 跳转页面
            this.$router.push('/index')
          })
          // 重新获取验证码图片
          this.getCaptcha()
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // note: 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // note: 获取验证码图片（base64编码）
    getCaptcha() {
      /*
        后端响应 response.data 的结构如下；
          {
            "code": 200,
            "msg": "响应信息",
            "data": {
              "token": "随机码",
              "captchaImg": "验证码图片的 base64 编码"
            }
          }
      */
      this.$request.get('/captcha').then(response => {
        // note: 后端返回 token 和 base64（验证码 code 的图片），并自己在 Redis 中存入 【token : code】 的键值对
        this.loginForm.token = response.data.data.token;
        this.captchaImg = response.data.data.captchaImg;
        // 获取图片后顺便清空一下 code
        this.loginForm.code = '';
      })
    }
  },
  // note: 生命周期：页面挂载完成时调用
  mounted() {
    // 获取验证码图片
    this.getCaptcha()
  }
};
</script>

<style scoped>
  /* note: 总体样式 */
  .el-row {
     height: 100%;
     background-color: #fafafa;
     display: flex;              /* note: 弹性布局 */
     align-items: center;        /* note: 纵向居中 */
     text-align: center;         /* note: 文本居中 */
  }
  /* note: 设置分割线长度 */
  .el-divider {
    height: 200px;
  }
  /* note：验证码图片样式 */
  .captchaImg {
    float: left;
    margin-left: 8px;
    border-radius: 4px;
  }
</style>