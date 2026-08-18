<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="科室编码" prop="departmentCode">
        <el-input
          v-model="queryParams.departmentCode"
          placeholder="请输入科室编码"
          clearable
          style="width: 220px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="科室名称" prop="departmentName">
        <el-input
          v-model="queryParams.departmentName"
          placeholder="请输入科室名称"
          clearable
          style="width: 220px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="科室类型" prop="departmentType">
        <el-input
          v-model="queryParams.departmentType"
          placeholder="请输入科室类型"
          clearable
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="科室状态" clearable style="width: 160px">
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['hospital:department:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['hospital:department:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['hospital:department:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hospital:department:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="departmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="科室ID" align="center" prop="departmentId" width="90" />
      <el-table-column label="科室编码" align="center" prop="departmentCode" :show-overflow-tooltip="true" />
      <el-table-column label="科室名称" align="center" prop="departmentName" :show-overflow-tooltip="true" />
      <el-table-column label="科室类型" align="center" prop="departmentType" :show-overflow-tooltip="true" />
      <el-table-column label="组织名称" align="center" prop="deptName" :show-overflow-tooltip="true" />
      <el-table-column label="负责人" align="center" prop="directorName" :show-overflow-tooltip="true" />
      <el-table-column label="联系电话" align="center" prop="phone" :show-overflow-tooltip="true" />
      <el-table-column label="位置" align="center" prop="location" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hospital:department:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hospital:department:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="科室编码" prop="departmentCode">
          <el-input v-model="form.departmentCode" placeholder="请输入科室编码" />
        </el-form-item>
        <el-form-item label="科室名称" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="请输入科室名称" />
        </el-form-item>
        <el-form-item label="归属组织" prop="deptId">
          <treeselect
            v-model="form.deptId"
            :options="deptOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="请选择归属组织"
          />
        </el-form-item>
        <el-form-item label="科室类型" prop="departmentType">
          <el-input v-model="form.departmentType" placeholder="请输入科室类型" />
        </el-form-item>
        <el-form-item label="负责人" prop="directorName">
          <el-input v-model="form.directorName" placeholder="请输入科室负责人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="科室位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入科室位置" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDepartment, getDepartment, delDepartment, addDepartment, updateDepartment } from "@/api/hospital/department"
import { listDept } from "@/api/system/dept"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "HospitalDepartment",
  dicts: ['sys_normal_disable'],
  components: { Treeselect },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      departmentList: [],
      deptOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        departmentCode: undefined,
        departmentName: undefined,
        departmentType: undefined,
        status: undefined
      },
      form: {},
      rules: {
        departmentCode: [
          { required: true, message: "科室编码不能为空", trigger: "blur" }
        ],
        departmentName: [
          { required: true, message: "科室名称不能为空", trigger: "blur" }
        ],
        deptId: [
          { required: true, message: "归属组织不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDeptOptions()
  },
  methods: {
    /** 查询科室列表 */
    getList() {
      this.loading = true
      listDepartment(this.queryParams).then(response => {
        this.departmentList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询归属组织树 */
    getDeptOptions() {
      listDept({ status: "0" }).then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId")
      })
    },
    /** 转换组织树字段 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        departmentId: undefined,
        deptId: undefined,
        departmentCode: undefined,
        departmentName: undefined,
        departmentType: undefined,
        directorName: undefined,
        phone: undefined,
        location: undefined,
        status: "0",
        remark: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加科室"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.departmentId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const departmentId = row.departmentId || this.ids
      getDepartment(departmentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改科室"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.departmentId != undefined) {
            updateDepartment(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDepartment(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const departmentIds = row.departmentId || this.ids
      this.$modal.confirm('是否确认删除科室编号为"' + departmentIds + '"的数据项？').then(function() {
        return delDepartment(departmentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('hospital/department/export', {
        ...this.queryParams
      }, `department_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
