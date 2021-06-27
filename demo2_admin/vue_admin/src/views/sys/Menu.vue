<template>
  <div>
    <!-- 新增按钮 -->
    <el-form :inline="true">
			<el-form-item>
				<el-button type="primary" @click="dialogVisible = true">新增</el-button>
			</el-form-item>
		</el-form>

    <!-- 展示表格 -->
    <el-table
      :data="tableData"
      style="width: 100%;margin-bottom: 20px;"
      row-key="id"
      border
      stripe
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">

      <el-table-column prop="name" label="名称" sortable width="180"></el-table-column>
			<el-table-column prop="perms" label="权限编码" sortable width="180"></el-table-column>
			<el-table-column prop="icon" label="图标"></el-table-column>
			<el-table-column prop="type" label="类型">
				<template slot-scope="scope">
					<el-tag size="small" v-if="scope.row.type === 0">目录</el-tag>
					<el-tag size="small" v-else-if="scope.row.type === 1" type="success">菜单</el-tag>
					<el-tag size="small" v-else-if="scope.row.type === 2" type="info">按钮</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="path" label="菜单URL"></el-table-column>
			<el-table-column prop="component" label="菜单组件"></el-table-column>
			<el-table-column prop="orderNum" label="排序号"></el-table-column>
			<el-table-column prop="statu" label="状态">
        <template slot-scope="scope">
					<el-tag size="small" v-if="scope.row.statu === 1" type="success">正常</el-tag>
					<el-tag size="small" v-else-if="scope.row.statu === 0" type="danger">禁用</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="icon" label="操作">
				<template slot-scope="scope">
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

    <!--表单对话框-->
		<el-dialog
				title="提示"
				:visible.sync="dialogVisible"
				width="600px"
				:before-close="handleClose">

			<el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-editForm">

				<el-form-item label="上级菜单" prop="parentId">
					<el-select v-model="editForm.parentId" placeholder="请选择上级菜单">
						<template v-for="item in tableData">
							<el-option :label="item.name" :value="item.id" :key="item.id"></el-option>
							<template v-for="child in item.children">
								<el-option :label="child.name" :value="child.id" :key="child.id">
									<span>{{ "- " + child.name }}</span>
								</el-option>
							</template>
						</template>
					</el-select>
				</el-form-item>

				<el-form-item label="菜单名称" prop="name" label-width="100px">
					<el-input v-model="editForm.name" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="权限编码" prop="perms" label-width="100px">
					<el-input v-model="editForm.perms" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="图标" prop="icon" label-width="100px">
					<el-input v-model="editForm.icon" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="菜单URL" prop="path" label-width="100px">
					<el-input v-model="editForm.path" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="菜单组件" prop="component" label-width="100px">
					<el-input v-model="editForm.component" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="类型" prop="type" label-width="100px">
					<el-radio-group v-model="editForm.type">
						<el-radio :label=0>目录</el-radio>
						<el-radio :label=1>菜单</el-radio>
						<el-radio :label=2>按钮</el-radio>
					</el-radio-group>
				</el-form-item>

				<el-form-item label="状态" prop="statu" label-width="100px">
					<el-radio-group v-model="editForm.statu">
						<el-radio :label=0>禁用</el-radio>
						<el-radio :label=1>正常</el-radio>
					</el-radio-group>
				</el-form-item>

				<el-form-item label="排序号" prop="orderNum" label-width="100px">
					<el-input-number v-model="editForm.orderNum" :min="1" label="排序号">1</el-input-number>
				</el-form-item>


				<el-form-item>
					<el-button type="primary" @click="submitForm('editForm')">立即创建</el-button>
					<el-button @click="resetForm('editForm')">重置</el-button>
				</el-form-item>
			</el-form>

		</el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Menu',
  data() {
    return {
      dialogVisible: false,    // 表单对话框的显示状态
      // 表单数据
      editForm: {},
      // 表单校验规则
      editFormRules: {
        parentId: [
          {required: true, message: '请选择上级菜单', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ],
        perms: [
          {required: true, message: '请输入权限编码', trigger: 'blur'}
        ],
        type: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ],
        orderNum: [
          {required: true, message: '请填入排序号', trigger: 'blur'}
        ],
        statu: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ]
      },
      tableData: []     // 表格展示的数据
    }
  },
  created() {
    this.getMenuTree()
  },
  methods: {
    // 获得 menu 的数据
    getMenuTree() {
      this.$request.get("/sys/menu/list").then(res => {
        this.tableData = res.data.data
      })
    },
    // 提交的表单数据
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // note: editForm 中没有 id 属性说明新增，有 id 属性说明是更新
          this.$request.post('/sys/menu/' + (this.editForm.id?'update' : 'save'), this.editForm).then(response => {
              // 提示操作成功 
              this.$message({
                showClose: true,
                message: '恭喜你，操作成功',
                type: 'success',
                // 关闭后重新获得数据
                onClose:() => {
                  this.getMenuTree()
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
      // 从后端获得该条数据的信息
      this.$request.get('/sys/menu/info/' + id).then(response => {
        // 放到表单数据中去，此时 id 有值
        this.editForm = response.data.data
        // 显示表单
        this.dialogVisible = true
      })
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false
      this.editForm = {}
    },
    // 表单对话框被关闭时，重置表单
    handleClose() {
      this.resetForm('editForm')
    },
    // 删除
    delHandle(id) {
      this.$request.post("/sys/menu/delete/" + id).then(response => {
        this.$message({
          showClose: true,
          message: '恭喜你，操作成功',
          type: 'success',
          onClose:() => {
            this.getMenuTree()
          }
        });
      })
    }
  }
}
</script>

<style scoped>
  
</style>