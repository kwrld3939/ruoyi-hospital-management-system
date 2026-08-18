<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="医生编码" prop="doctorCode">
        <el-input
          v-model="queryParams.doctorCode"
          placeholder="请输入医生编码"
          clearable
          style="width: 220px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生姓名" prop="doctorName">
        <el-input
          v-model="queryParams.doctorName"
          placeholder="请输入医生姓名"
          clearable
          style="width: 220px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属科室" prop="departmentId">
        <el-select v-model="queryParams.departmentId" placeholder="请选择科室" clearable filterable style="width: 220px">
          <el-option
            v-for="item in deptOptions"
            :key="item.departmentId"
            :label="item.departmentName"
            :value="item.departmentId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="职称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入职称"
          clearable
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="医生状态" clearable style="width: 160px">
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
          v-hasPermi="['hospital:doctor:add']"
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
          v-hasPermi="['hospital:doctor:edit']"
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
          v-hasPermi="['hospital:doctor:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hospital:doctor:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="doctorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="医生ID" align="center" prop="doctorId" width="90" />
      <el-table-column label="医生编码" align="center" prop="doctorCode" :show-overflow-tooltip="true" />
      <el-table-column label="医生姓名" align="center" prop="doctorName" :show-overflow-tooltip="true" />
      <el-table-column label="所属科室" align="center" prop="departmentName" :show-overflow-tooltip="true" />
      <el-table-column label="组织名称" align="center" prop="deptName" :show-overflow-tooltip="true" />
      <el-table-column label="绑定账号" align="center" prop="userName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ scope.row.userName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="性别" align="center" prop="gender">
        <template slot-scope="scope">
          <span v-if="scope.row.gender === '0'">男</span>
          <span v-else-if="scope.row.gender === '1'">女</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="职称" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="专长" align="center" prop="specialty" :show-overflow-tooltip="true" />
      <el-table-column label="联系电话" align="center" prop="phone" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
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
            v-hasPermi="['hospital:doctor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hospital:doctor:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="医生编码" prop="doctorCode">
          <el-input v-model="form.doctorCode" placeholder="请输入医生编码" />
        </el-form-item>
        <el-form-item label="医生姓名" prop="doctorName">
          <el-input v-model="form.doctorName" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="所属科室" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择科室" filterable style="width: 100%">
            <el-option
              v-for="item in deptOptions"
              :key="item.departmentId"
              :label="item.departmentName"
              :value="item.departmentId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="form.title" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="专长" prop="specialty">
          <el-input v-model="form.specialty" type="textarea" placeholder="请输入专长" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="绑定账号" prop="userId">
          <el-select
            v-model="form.userId"
            placeholder="输入账号或昵称搜索"
            clearable
            filterable
            remote
            reserve-keyword
            :remote-method="remoteQueryUsers"
            :loading="userLoading"
            style="width: 100%"
          >
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.userName"
              :value="item.userId"
            />
          </el-select>
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
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listDoctor, getDoctor, delDoctor, addDoctor, updateDoctor } from "@/api/hospital/doctor"
import { listDepartment } from "@/api/hospital/department"
import { listUser } from "@/api/system/user"

export default {
  name: "HospitalDoctor",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      doctorList: [],
      deptOptions: [],
      userOptions: [],
      userLoading: false,
      userTimer: null,
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        doctorCode: undefined,
        doctorName: undefined,
        departmentId: undefined,
        title: undefined,
        status: undefined
      },
      form: {},
      rules: {
        doctorCode: [
          { required: true, message: "医生编码不能为空", trigger: "blur" }
        ],
        doctorName: [
          { required: true, message: "医生姓名不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "性别不能为空", trigger: "change" }
        ],
        departmentId: [
          { required: true, message: "所属科室不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getDeptOptions()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listDoctor(this.queryParams).then(response => {
        this.doctorList = response.rows
        this.total = response.total
      }).finally(() => {
        this.loading = false
      })
    },
    getDeptOptions() {
      listDepartment({ pageNum: 1, pageSize: 1000, status: '0' }).then(response => {
        this.deptOptions = response.rows || []
      })
    },
    remoteQueryUsers(keyword) {
      clearTimeout(this.userTimer)
      this.userTimer = setTimeout(() => {
        const query = { pageNum: 1, pageSize: 20 }
        if (keyword && keyword.trim()) {
          query.userName = keyword.trim()
        }
        this.userLoading = true
        listUser(query).then(response => {
          this.userOptions = (response.rows || []).map(item => ({
            userId: item.userId,
            userName: item.userName + (item.nickName ? `（${item.nickName}）` : '')
          }))
        }).finally(() => {
          this.userLoading = false
        })
      }, 300)
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        doctorId: undefined,
        departmentId: undefined,
        userId: undefined,
        doctorCode: undefined,
        doctorName: undefined,
        gender: undefined,
        title: undefined,
        specialty: undefined,
        phone: undefined,
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
      this.userOptions = []
      this.open = true
      this.title = "新增医生"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.doctorId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const doctorId = row.doctorId || this.ids
      getDoctor(doctorId).then(response => {
        this.form = response.data
        this.userOptions = this.form.userId ? [{
          userId: this.form.userId,
          userName: this.form.userName || ''
        }] : []
        this.open = true
        this.title = "修改医生"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.doctorId != undefined) {
            updateDoctor(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDoctor(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const doctorIds = row.doctorId || this.ids
      this.$modal.confirm('是否确认删除医生编号为"' + doctorIds + '"的数据项？').then(function() {
        return delDoctor(doctorIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('hospital/doctor/export', {
        ...this.queryParams
      }, `doctor_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
