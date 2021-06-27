<template>
	<div style="text-align: center;">
		<h2>你好！{{ userInfo.username }} 同学</h2>

		<el-form :model="passForm" status-icon :rules="rules" ref="passForm" label-width="100px">
			<el-form-item label="旧密码" prop="currentPass">
				<el-input type="password" v-model="passForm.currentPass" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="新密码" prop="password">
				<el-input type="password" v-model="passForm.password" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="确认密码" prop="checkPass">
				<el-input type="password" v-model="passForm.checkPass" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="submitForm('passForm')">提交</el-button>
				<el-button @click="resetForm('passForm')">重置</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>

	export default {
		name: "UserCenter",
		data() {
      // note: 定义的一个验证规则，用于输入校验
			var validatePass = (rule, value, callback) => {
				if (value === '') {
					callback(new Error('请再次输入密码'));
				} else if (value !== this.passForm.password) {
					callback(new Error('两次输入密码不一致!'));
				} else {
					callback();
				}
			};
			return {
        // note: 用户信息数据，从后端获取
				userInfo: {

				},
        // note: 表单数据
				passForm: {
					password: '',      // 新密码
					checkPass: '',     // 确认密码
					currentPass: ''    // 旧密码
				},
        // note: 输入校验
				rules: {
					password: [
						{ required: true, message: '请输入新密码', trigger: 'blur' },
						{ min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
					],
					checkPass: [
						{ required: true, validator: validatePass, trigger: 'blur' }
					],
					currentPass: [
						{ required: true, message: '请输入当前密码', trigger: 'blur' },
					]
				}
			}
		},
		created() {
			this.getUserInfo()
		},
		methods: {
      // note: 请求后端获取用户信息
			getUserInfo() {
				this.$request.get("/sys/userInfo").then(res => {
					this.userInfo = res.data.data;
				})
			},
      // note: 提交新的密码到后端
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {

						const _this = this
						this.$request.post('/sys/user/updatePass', this.passForm).then(response => {
              // note: 弹窗提示
							_this.$alert(response.data.msg, '提示', {
								confirmButtonText: '确定',
								callback: action => {
									this.$refs[formName].resetFields();    // 清空表单
								}
							});
						})

					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
      // note: 清空表单
			resetForm(formName) {
				this.$refs[formName].resetFields();
			}
		}
	}
</script>

<style scoped>
/* note: 总体样式 */
.el-form {
	width: 420px;
	margin: 50px auto;
}
</style>