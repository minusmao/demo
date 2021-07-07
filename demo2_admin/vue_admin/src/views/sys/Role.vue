<template>
  <div>
    <!-- note: 行内表单：搜索、新增、批量删除 -->
    <el-form :inline="true">
			<el-form-item>
				<el-input
						v-model="searchForm.name"
						placeholder="名称"
						clearable
				>
				</el-input>
			</el-form-item>

			<el-form-item>
				<el-button @click="getRoleList()">搜索</el-button>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="dialogVisible = true">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-popconfirm title="这是确定批量删除吗？" @confirm="delHandle(null)">
					<el-button type="danger" slot="reference" :disabled="delBtlStatu">批量删除</el-button>
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
			<el-table-column prop="name" label="名称" width="120"></el-table-column>
			<el-table-column prop="code" label="唯一编码" width="150" show-overflow-tooltip></el-table-column>
			<el-table-column prop="remark" label="描述" show-overflow-tooltip></el-table-column>
			<el-table-column prop="statu" label="状态" width="120">
				<template slot-scope="scope">
					<el-tag size="small" v-if="scope.row.statu === 1" type="success">正常</el-tag>
					<el-tag size="small" v-else-if="scope.row.statu === 0" type="danger">禁用</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="icon" label="操作" width="240">
				<template slot-scope="scope">
					<el-button type="text" @click="permHandle(scope.row.id)">分配权限</el-button>
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

    <!-- 新增、更新表单对话框 -->
		<el-dialog
				title="提示"
				:visible.sync="dialogVisible"
				width="600px"
				:before-close="handleClose">

			<el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-editForm">

				<el-form-item label="角色名称" prop="name" label-width="100px">
					<el-input v-model="editForm.name" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="唯一编码" prop="code" label-width="100px">
					<el-input v-model="editForm.code" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="描述" prop="remark" label-width="100px">
					<el-input v-model="editForm.remark" autocomplete="off"></el-input>
				</el-form-item>


				<el-form-item label="状态" prop="statu" label-width="100px">
					<el-radio-group v-model="editForm.statu">
						<el-radio :label=0>禁用</el-radio>
						<el-radio :label=1>正常</el-radio>
					</el-radio-group>
				</el-form-item>

				<el-form-item>
					<el-button type="primary" @click="submitForm('editForm')">立即创建</el-button>
					<el-button @click="resetForm('editForm')">重置</el-button>
				</el-form-item>
			</el-form>

		</el-dialog>

    <!-- 分配权限表单 -->
    <el-dialog
				title="分配权限"
				:visible.sync="permDialogVisible"
				width="600px">

			<el-form :model="permForm">
				<el-tree
						:data="permTreeData"
						show-checkbox
						ref="permTree"
						:default-expand-all=true
						node-key="id"
						:check-strictly=true
						:props="defaultProps">
				</el-tree>
			</el-form>

			<span slot="footer" class="dialog-footer">
			    <el-button @click="permDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="submitPermFormHandle('permForm')">确 定</el-button>
			</span>

		</el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Role',
  data() {
    return {
      // 行内表单
      searchForm: {},        // 行内表单 -> 搜索
      delBtlStatu: true,     // 行内表单 -> 批量删除按钮的使能，默认不使能，当勾选数据后，才使能
      // 表格
      tableData: [],         // 表格展示的数据
      multipleSelection: [],     // 被选中的数据
      // 分页数据
      total: 0,       // 数据总数
      size: 10,       // 每页的数量
      current: 1,     // 当前第几页
      // 新增、更新表单
      dialogVisible: false,    // 表单对话框的显示状态
      editForm: {},            // 表单数据
      editFormRules: {         // 表单输入规则
        name: [
          {required: true, message: '请输入角色名称', trigger: 'blur'}
        ],
        code: [
          {required: true, message: '请输入唯一编码', trigger: 'blur'}
        ],
        statu: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ]
      },
      // 分配权限表单
      permDialogVisible: false,    // 表单对话框的显示状态
      permForm: {},                // 表单数据
      permTreeData: [],            // 展示权限
      // 权限表单展示的内容跟 permTreeData 中属性的关系
      defaultProps: {
        children: 'children',
        label: 'name'
			}
    }
  },
  created() {
    // 获得 role 的表格数据
    this.getRoleList()
    // 获得 menu 数据，用于展示 role 在 menu 上的权限
    this.$request.get('/sys/menu/list').then(res => {
      this.permTreeData = res.data.data
    })
  },
  methods: {
    // 获得表格数据
    getRoleList() {
      this.$request.get("/sys/role/list", {
        params: {
          name: this.searchForm.name,    // 行内表单的搜索框数据
          current: this.current,         // 分页组件的第几页
          size: this.size                // 分页组件的每页数量
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
      this.getRoleList()
    },
    // 改变当前页（current）
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.current = val
      this.getRoleList()
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
          this.$request.post('/sys/role/' + (this.editForm.id? 'update' : 'save'), this.editForm)
            .then(response => {

              this.$message({
                showClose: true,
                message: '恭喜你，操作成功',
                type: 'success',
                // 关闭后更新表格显示的数据
                onClose:() => {
                  this.getRoleList()
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
      this.$request.get('/sys/role/info/' + id).then(response => {
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
      this.$request.post("/sys/role/delete", ids).then(response => {
        this.$message({
          showClose: true,
          message: '恭喜你，操作成功',
          type: 'success',
          onClose:() => {
            this.getRoleList()
          }
        });
      })
    },
    // 打开权限对话框
    permHandle(id) {
      this.permDialogVisible = true
      // 根据 id 获得该角色已有的权限
      this.$request.get("/sys/role/info/" + id).then(response => {
        this.$refs.permTree.setCheckedKeys(response.data.data.menuIds)
        this.permForm = response.data.data
      })
    },
    // 提交权限表单
    submitPermFormHandle(formName) {
      // 得到被选中的权限
      var menuIds = this.$refs.permTree.getCheckedKeys()

      this.$request.post('/sys/role/perm/' + this.permForm.id, menuIds).then(response => {
        this.$message({
          showClose: true,
          message: '恭喜你，操作成功',
          type: 'success',
          onClose:() => {
            this.getRoleList()
          }
        });
        // 关闭对话框
        this.permDialogVisible = false
      })
    }
  }
}
</script>

<style scoped>
  /* 分页组件样式 */
  .el-pagination {
		float: right;
		margin-top: 22px;
	}
</style>