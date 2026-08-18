<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="科室" prop="departmentId">
        <el-select
          v-model="queryParams.departmentId"
          placeholder="请选择科室"
          clearable
          filterable
          style="width: 180px"
          @change="handleQueryDepartmentChange"
        >
          <el-option
            v-for="item in deptOptions"
            :key="item.departmentId"
            :label="item.departmentName"
            :value="item.departmentId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="医生" prop="doctorId">
        <el-select
          v-model="queryParams.doctorId"
          placeholder="输入姓名或编码搜索"
          clearable
          filterable
          remote
          reserve-keyword
          :remote-method="remoteQueryDoctors"
          :loading="queryDoctorLoading"
          style="width: 220px"
        >
          <el-option
            v-for="item in queryDoctorOptions"
            :key="item.doctorId"
            :label="item.doctorName + '（' + item.doctorCode + '）'"
            :value="item.doctorId"
          >
            <span style="float: left">{{ item.doctorName }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.doctorCode }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="scheduleDate">
        <el-date-picker
          v-model="queryParams.scheduleDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择日期"
          style="width: 160px"
        />
      </el-form-item>
      <el-form-item label="时间段" prop="timeSlot">
        <el-select v-model="queryParams.timeSlot" placeholder="时间段" clearable style="width: 120px">
          <el-option
            v-for="item in timeSlotOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="排班状态" clearable style="width: 140px">
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
          v-hasPermi="['hospital:schedule:add']"
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
          v-hasPermi="['hospital:schedule:edit']"
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
          v-hasPermi="['hospital:schedule:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hospital:schedule:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="scheduleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="排班ID" align="center" prop="scheduleId" width="90" />
      <el-table-column label="科室" align="center" prop="departmentName" :show-overflow-tooltip="true" />
      <el-table-column label="医生" align="center" prop="doctorName" :show-overflow-tooltip="true" />
      <el-table-column label="职称" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="排班日期" align="center" prop="scheduleDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.scheduleDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间段" align="center" prop="timeSlot" width="90">
        <template slot-scope="scope">
          <span>{{ formatTimeSlot(scope.row.timeSlot) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出诊地点" align="center" prop="location" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status" width="90">
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
            v-hasPermi="['hospital:schedule:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hospital:schedule:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="640px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="96px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="科室" prop="departmentId">
              <el-select
                v-model="form.departmentId"
                placeholder="请选择科室"
                filterable
                style="width: 100%"
                @change="handleFormDepartmentChange"
              >
                <el-option
                  v-for="item in deptOptions"
                  :key="item.departmentId"
                  :label="item.departmentName"
                  :value="item.departmentId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医生" prop="doctorId">
              <el-select
                v-model="form.doctorId"
                placeholder="输入姓名或编码搜索"
                clearable
                filterable
                remote
                reserve-keyword
                :remote-method="remoteFormDoctors"
                :loading="formDoctorLoading"
                style="width: 100%"
              >
                <el-option
                  v-for="item in formDoctorOptions"
                  :key="item.doctorId"
                  :label="item.doctorName + '（' + item.doctorCode + '）'"
                  :value="item.doctorId"
                >
                  <span style="float: left">{{ item.doctorName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.doctorCode }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排班日期" prop="scheduleDate">
              <el-date-picker
                v-model="form.scheduleDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择排班日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="时间段" prop="timeSlot">
              <el-select v-model="form.timeSlot" placeholder="请选择时间段" style="width: 100%">
                <el-option
                  v-for="item in timeSlotOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="出诊地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入出诊地点" />
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
import { listSchedule, getSchedule, delSchedule, addSchedule, updateSchedule } from "@/api/hospital/schedule"
import { listDepartment } from "@/api/hospital/department"
import { listDoctor } from "@/api/hospital/doctor"

export default {
  name: "HospitalSchedule",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      scheduleList: [],
      deptOptions: [],
      queryDoctorOptions: [],
      formDoctorOptions: [],
      queryDoctorLoading: false,
      formDoctorLoading: false,
      queryDoctorTimer: null,
      formDoctorTimer: null,
      timeSlotOptions: [
        { label: "上午", value: "1" },
        { label: "下午", value: "2" },
        { label: "晚上", value: "3" }
      ],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        departmentId: undefined,
        doctorId: undefined,
        scheduleDate: undefined,
        timeSlot: undefined,
        status: undefined
      },
      form: {},
      rules: {
        departmentId: [
          { required: true, message: "所属科室不能为空", trigger: "change" }
        ],
        doctorId: [
          { required: true, message: "出诊医生不能为空", trigger: "change" }
        ],
        scheduleDate: [
          { required: true, message: "排班日期不能为空", trigger: "change" }
        ],
        timeSlot: [
          { required: true, message: "时间段不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getDeptOptions()
    this.getList()
  },
  methods: {
    /** 查询医生排班列表 */
    getList() {
      this.loading = true
      listSchedule(this.queryParams).then(response => {
        this.scheduleList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getDeptOptions() {
      listDepartment({ pageNum: 1, pageSize: 1000, status: "0" }).then(response => {
        this.deptOptions = response.rows || []
      })
    },
    getDoctorOptions(type, departmentId, keyword) {
      const query = { pageNum: 1, pageSize: 20, status: "0" }
      if (departmentId) {
        query.departmentId = departmentId
      }
      if (keyword) {
        query.doctorKeyword = keyword
      }
      if (type === "form") {
        this.formDoctorLoading = true
      } else {
        this.queryDoctorLoading = true
      }
      listDoctor(query).then(response => {
        if (type === "form") {
          this.formDoctorOptions = response.rows || []
          this.formDoctorLoading = false
        } else {
          this.queryDoctorOptions = response.rows || []
          this.queryDoctorLoading = false
        }
      }).catch(() => {
        if (type === "form") {
          this.formDoctorLoading = false
        } else {
          this.queryDoctorLoading = false
        }
      })
    },
    remoteQueryDoctors(keyword) {
      clearTimeout(this.queryDoctorTimer)
      this.queryDoctorTimer = setTimeout(() => {
        if (!keyword || !keyword.trim()) {
          this.queryDoctorOptions = []
          return
        }
        this.getDoctorOptions("query", this.queryParams.departmentId, keyword.trim())
      }, 300)
    },
    remoteFormDoctors(keyword) {
      clearTimeout(this.formDoctorTimer)
      this.formDoctorTimer = setTimeout(() => {
        if (!keyword || !keyword.trim()) {
          this.formDoctorOptions = this.form.doctorId ? [this.form] : []
          return
        }
        this.getDoctorOptions("form", this.form.departmentId, keyword.trim())
      }, 300)
    },
    handleQueryDepartmentChange(value) {
      this.queryParams.doctorId = undefined
      this.queryDoctorOptions = []
    },
    handleFormDepartmentChange(value) {
      this.form.doctorId = undefined
      this.formDoctorOptions = []
    },
    formatTimeSlot(value) {
      const item = this.timeSlotOptions.find(option => option.value === value)
      return item ? item.label : "-"
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        scheduleId: undefined,
        departmentId: undefined,
        doctorId: undefined,
        scheduleDate: undefined,
        timeSlot: undefined,
        location: undefined,
        status: "0",
        remark: undefined
      }
      this.formDoctorOptions = []
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.queryDoctorOptions = []
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加医生排班"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.scheduleId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const scheduleId = row.scheduleId || this.ids
      getSchedule(scheduleId).then(response => {
        this.form = response.data
        this.formDoctorOptions = [this.form]
        this.open = true
        this.title = "修改医生排班"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.scheduleId != undefined) {
            updateSchedule(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSchedule(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const scheduleIds = row.scheduleId || this.ids
      this.$modal.confirm('是否确认删除医生排班编号为"' + scheduleIds + '"的数据项？').then(function() {
        return delSchedule(scheduleIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('hospital/schedule/export', {
        ...this.queryParams
      }, `schedule_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
