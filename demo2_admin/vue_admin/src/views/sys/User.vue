<template>
	<div>
    <!-- 行内表单:搜索、新增、批量删除 -->
		<el-form :inline="true">
			<el-form-item>
				<el-input
						v-model="searchForm.username"
						placeholder="用户名"
						clearable
				>
				</el-input>
			</el-form-item>

			<el-form-item>
				<el-button @click="getUserList()">搜索</el-button>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="dialogVisible = true" v-if="hasAuth('sys:user:save')">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-popconfirm title="这是确定批量删除吗？" @confirm="delHandle(null)">
					<el-button type="danger" slot="reference" :disabled="delBtlStatu" v-if="hasAuth('sys:user:delete')">批量删除</el-button>
				</el-popconfirm>
			</el-form-item>
		</el-form>

    <!-- 展示表格 -->
		<el-table
				ref="multipleTable"
				:data="tableData"
				tooltip-effect="dark"
				style="width: 100%"
				border
				stripe
				@selection-change="handleSelectionChange">

			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column label="头像" width="50">
				<template slot-scope="scope">
					<el-avatar size="small" :src="scope.row.avatar"></el-avatar>
				</template>
			</el-table-column>
			<el-table-column prop="username" label="用户名" width="120"></el-table-column>
			<el-table-column prop="code" label="角色名称">
				<template slot-scope="scope">
					<el-tag size="small" type="info" v-for="item in scope.row.sysRoles" :key="item.id">{{item.name}}</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="email" label="邮箱"></el-table-column>
			<el-table-column prop="phone" label="手机号"></el-table-column>
			<el-table-column prop="statu" label="状态">
				<template slot-scope="scope">
					<el-tag size="small" v-if="scope.row.statu === 1" type="success">正常</el-tag>
					<el-tag size="small" v-else-if="scope.row.statu === 0" type="danger">禁用</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="created" width="200" label="创建时间"></el-table-column>
      
			<el-table-column prop="icon" width="260px" label="操作">

				<template slot-scope="scope">
					<el-button type="text" @click="roleHandle(scope.row.id)">分配角色</el-button>
					<el-divider direction="vertical"></el-divider>

					<el-button type="text" @click="repassHandle(scope.row.id, scope.row.username)">重置密码</el-button>
					<el-divider direction="vertical"></el-divider>

					<el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
					<el-divider direction="vertical"></el-divider>

					<template>
						<el-popconfirm title="这是一段内容确定删除吗？" @confirm="delHandle(scope.row.id)">
							<el-button type="text" slot="reference">删除</el-button>
						</el-popconfirm>
					</template>

				</template>
			</el-table-column>

		</el-table>

    <!-- 分页组件 -->
		<el-pagination
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				layout="total, sizes, prev, pager, next, jumper"
				:page-sizes="[10, 20, 50, 100]"
				:current-page="current"
				:page-size="size"
				:total="total">
		</el-pagination>


		<!-- 新增对话框 -->
		<el-dialog
				title="提示"
				:visible.sync="dialogVisible"
				width="600px"
				:before-close="handleClose">

			<el-form :model="editForm" :rules="editFormRules" ref="editForm">
				<el-form-item label="用户名" prop="username" label-width="100px">
					<el-input v-model="editForm.username" autocomplete="off"></el-input>
					<el-alert
							title="初始密码为888888"
							:closable="false"
							type="info"
							style="line-height: 12px;"
					></el-alert>
				</el-form-item>

				<el-form-item label="邮箱"  prop="email" label-width="100px">
					<el-input v-model="editForm.email" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="手机号"  prop="phone" label-width="100px">
					<el-input v-model="editForm.phone" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="状态"  prop="statu" label-width="100px">
					<el-radio-group v-model="editForm.statu">
						<el-radio :label="0">禁用</el-radio>
						<el-radio :label="1">正常</el-radio>
					</el-radio-group>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="resetForm('editForm')">取 消</el-button>
				<el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 分配角色对话框 -->
		<el-dialog title="分配角色" :visible.sync="roleDialogFormVisible" width="600px">

			<el-form :model="roleForm">
				<el-tree
						:data="roleTreeData"
						show-checkbox
						ref="roleTree"
						:check-strictly=checkStrictly
						node-key="id"
						:default-expand-all=true
						:props="defaultProps">
				</el-tree>
			</el-form>

			<div slot="footer" class="dialog-footer">
				<el-button @click="roleDialogFormVisible=false">取 消</el-button>
				<el-button type="primary" @click="submitRoleHandle('roleForm')">确 定</el-button>
			</div>
		</el-dialog>

	</div>
</template>

<script>
	export default {
		name: "Role",
		data() {
			return {
        // 行内表单
				searchForm: {},        // 行内表单 -> 搜索
				delBtlStatu: true,     // 行内表单 -> 批量删除按钮的使能，默认不使能，当勾选数据后，才使能
        // 分页组件
				total: 0,       // 总数
				size: 10,       // 每页个数
				current: 1,     // 第几页
        // 新增、更新表单
				dialogVisible: false,    // 表单对话框的显示状态
				editForm: {},            // 表单数据
        // 新增表单的输入校验
				editFormRules: {
					username: [
						{required: true, message: '请输入用户名称', trigger: 'blur'}
					],
					email: [
						{required: true, message: '请输入邮箱', trigger: 'blur'}
					],
					statu: [
						{required: true, message: '请选择状态', trigger: 'blur'}
					]
				},
        // 表格
				tableData: [],           // 表格展示的数据
        multipleSelection: [],   // 被选中的数据的 id
        // 分配角色表单
				roleDialogFormVisible: false,    // 表单对话框的显示状态
        roleForm: {},                    // 表单数据
				roleTreeData:  [],               // 展示角色
        // 角色表单展示的内容跟 roleTreeData 中属性的关系
				defaultProps: {
					children: 'children',
					label: 'name'
				},
				checkStrictly: true    // 分配角色对话框的属性，Role.js文件中直接为 true
			}
		},
		created() {
      // 获得 user 数据
			this.getUserList()
      // 获得 role 数据，用于展示 user 拥有的权限
			this.$request.get("/sys/role/list").then(response => {
				this.roleTreeData = response.data.data.records
			})
		},
		methods: {
      // 获得用户信息
			getUserList() {
				this.$request.get("/sys/user/list", {
					params: {
						username: this.searchForm.username,    // 行内表单的搜索框数据
						current: this.current,                 // 分页组件的第几页
						size: this.size                        // 分页组件的每页数量
					}
				}).then(response => {
          // 后端返回的表格数据
					this.tableData = response.data.data.records
          // 后端返回的分页数据
					this.size = response.data.data.size
					this.current = response.data.data.current
					this.total = response.data.data.total
				})
			},
      // 表格勾选变化时
			handleSelectionChange(val) {
				console.log("勾选")
				console.log(val)
				this.multipleSelection = val;

				this.delBtlStatu = val.length == 0    // 是否使能批量删除按键
			},
      // 改变每页显示的数量（total）
			handleSizeChange(val) {
				console.log(`每页 ${val} 条`);
				this.size = val
				this.getUserList()
			},
      // 改变当前页（current）
			handleCurrentChange(val) {
				console.log(`当前页: ${val}`);
				this.current = val
				this.getUserList()
			},
      // 重置表单并关闭
			resetForm(formName) {
				this.$refs[formName].resetFields();
				this.dialogVisible = false
				this.editForm = {}
			},
      // 表单对话框关闭时，重置表单
			handleClose() {
				this.resetForm('editForm')
			},
      // 提交新增、更新表单
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
            // note: editFrom 中没有 id 属性说明新增，有 id 属性说明是更新
						this.$request.post('/sys/user/' + (this.editForm.id?'update' : 'save'), this.editForm)
							.then(response => {

								this.$message({
									showClose: true,
									message: '恭喜你，操作成功',
									type: 'success',
                  // 关闭后更新表格显示的数据
									onClose:() => {
										this.getUserList()
									}
								});
                // 重置表单
                this.resetForm(formName)
							})
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
      // 编辑并更新，请求连接附带编辑对象的 id
			editHandle(id) {
        // 先从后端获得该条数据的信息
				this.$request.get('/sys/user/info/' + id).then(response => {
          // 放到表单数据 editForm 中去，此时 id 有值
					this.editForm = response.data.data
          // 显示表单
					this.dialogVisible = true
				})
			},
      // 删除数据
			delHandle(id) {
				var ids = []
        // 得到被勾选的数据，如果没传入 id 值，说明是批量删除
				if (id) {
					ids.push(id)
				} else {
					this.multipleSelection.forEach(row => {
						ids.push(row.id)
					})
				}
        // 提交到后端
				this.$request.post("/sys/user/delete", ids).then(response => {
					this.$message({
						showClose: true,
						message: '恭喜你，操作成功',
						type: 'success',
						onClose:() => {
							this.getUserList()
						}
					});
				})
			},
      // 打开分配角色对话框
			roleHandle (id) {
				this.roleDialogFormVisible = true
        // 根据 id 获得该用户已分配的角色
				this.$request.get('/sys/user/info/' + id).then(response => {
          // 放到角色表单中
					this.roleForm = response.data.data
          // 得到已有的角色的id
					let roleIds = []
					response.data.data.sysRoles.forEach(row => {
						roleIds.push(row.id)
					})
          // 设置已有的角色被选中
					this.$refs.roleTree.setCheckedKeys(roleIds)
				})
			},
      // 提交角色表单
			submitRoleHandle(formName) {
        // 获得被选中的角色 id
				var roleIds = this.$refs.roleTree.getCheckedKeys()

				this.$request.post('/sys/user/role/' + this.roleForm.id, roleIds).then(response => {
					this.$message({
						showClose: true,
						message: '恭喜你，操作成功',
						type: 'success',
						onClose:() => {
							this.getUserList()
						}
					});
          // 关闭对话框
					this.roleDialogFormVisible = false
				})
			},
      // 重置密码
			repassHandle(id, username) {
        // 提示
				this.$confirm('将重置用户【' + username + '】的密码, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
          // 向后端发送数据
					this.$request.post("/sys/user/repass", id).then(response => {
						this.$message({
							showClose: true,
							message: '恭喜你，操作成功',
							type: 'success',
							onClose: () => {
                this.getUserList()
							}
						});
					})
				})
			}
		}
	}
</script>

<style scoped>

	.el-pagination {
		float: right;
		margin-top: 22px;
	}

</style>