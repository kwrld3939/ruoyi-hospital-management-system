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
        <el-select v-model="queryParams.status" placeholder="号源状态" clearable style="width: 140px">
          <el-option
            v-for="item in sourceStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
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
          v-hasPermi="['hospital:source:add']"
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
          v-hasPermi="['hospital:source:edit']"
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
          v-hasPermi="['hospital:source:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hospital:source:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="号源ID" align="center" prop="sourceId" width="90" />
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
      <el-table-column label="总号数" align="center" prop="totalNum" width="90" />
      <el-table-column label="剩余号数" align="center" prop="remainNum" width="90" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag :type="formatSourceStatusType(scope.row.status)">{{ formatSourceStatus(scope.row.status) }}</el-tag>
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
            v-hasPermi="['hospital:source:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hospital:source:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="96px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="科室" prop="departmentId">
              <el-select
                v-model="form.departmentId"
                placeholder="请选择科室"
                clearable
                filterable
                :disabled="isEdit"
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
                :disabled="isEdit"
                style="width: 100%"
                @change="handleFormDoctorChange"
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
            <el-form-item label="日期" prop="scheduleDate">
              <el-date-picker
                v-model="form.scheduleDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
                :disabled="isEdit"
                style="width: 100%"
                @change="handleScheduleFilterChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="时间段" prop="timeSlot">
              <el-select
                v-model="form.timeSlot"
                placeholder="请选择时间段"
                clearable
                :disabled="isEdit"
                style="width: 100%"
                @change="handleScheduleFilterChange"
              >
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
        <el-form-item label="排班" prop="scheduleId">
          <el-select
            v-model="form.scheduleId"
            placeholder="请选择排班"
            filterable
            clearable
            :loading="scheduleLoading"
            :disabled="isEdit"
            style="width: 100%"
            @visible-change="handleScheduleVisible"
            @change="handleScheduleChange"
          >
            <el-option
              v-for="item in formScheduleOptions"
              :key="item.scheduleId"
              :label="formatScheduleLabel(item)"
              :value="item.scheduleId"
            />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="总号数" prop="totalNum">
              <el-input-number v-model="form.totalNum" :min="0" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="剩余号数" prop="remainNum">
              <el-input-number v-model="form.remainNum" :min="0" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="item in sourceStatusOptions"
              :key="item.value"
              :label="item.value"
            >{{ item.label }}</el-radio>
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
import { listSource, getSource, delSource, addSource, updateSource } from "@/api/hospital/source"
import { listDepartment } from "@/api/hospital/department"
import { listDoctor } from "@/api/hospital/doctor"
import { listSchedule } from "@/api/hospital/schedule"

export default {
  name: "HospitalSource",
  data() {
    const validateRemain = (rule, value, callback) => {
      if (value === undefined || value === null) {
        callback(new Error("剩余号数不能为空"))
      } else if (this.form.totalNum !== undefined && this.form.totalNum !== null && value > this.form.totalNum) {
        callback(new Error("剩余号数不能大于总号数"))
      } else {
        callback()
      }
    }
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      sourceList: [],
      deptOptions: [],
      queryDoctorOptions: [],
      formDoctorOptions: [],
      formScheduleOptions: [],
      queryDoctorLoading: false,
      formDoctorLoading: false,
      scheduleLoading: false,
      queryDoctorTimer: null,
      formDoctorTimer: null,
      timeSlotOptions: [
        { label: "上午", value: "1" },
        { label: "下午", value: "2" },
        { label: "晚上", value: "3" }
      ],
      sourceStatusOptions: [
        { label: "可预约", value: "0", type: "success" },
        { label: "停用", value: "1", type: "info" },
        { label: "约满", value: "2", type: "danger" }
      ],
      title: "",
      open: false,
      isEdit: false,
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
        scheduleId: [
          { required: true, message: "排班不能为空", trigger: "change" }
        ],
        totalNum: [
          { required: true, message: "总号数不能为空", trigger: "blur" }
        ],
        remainNum: [
          { validator: validateRemain, trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getDeptOptions()
    this.getList()
  },
  methods: {
    /** 查询号源列表 */
    getList() {
      this.loading = true
      listSource(this.queryParams).then(response => {
        this.sourceList = response.rows
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
    getScheduleOptions() {
      const query = { pageNum: 1, pageSize: 20, status: "0" }
      if (this.form.departmentId) {
        query.departmentId = this.form.departmentId
      }
      if (this.form.doctorId) {
        query.doctorId = this.form.doctorId
      }
      if (this.form.scheduleDate) {
        query.scheduleDate = this.form.scheduleDate
      }
      if (this.form.timeSlot) {
        query.timeSlot = this.form.timeSlot
      }
      this.scheduleLoading = true
      listSchedule(query).then(response => {
        this.formScheduleOptions = response.rows || []
        this.scheduleLoading = false
      }).catch(() => {
        this.scheduleLoading = false
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
    handleQueryDepartmentChange() {
      this.queryParams.doctorId = undefined
      this.queryDoctorOptions = []
    },
    handleFormDepartmentChange() {
      this.form.doctorId = undefined
      this.form.scheduleId = undefined
      this.formDoctorOptions = []
      this.formScheduleOptions = []
    },
    handleFormDoctorChange() {
      this.form.scheduleId = undefined
      this.formScheduleOptions = []
      this.handleScheduleFilterChange()
    },
    handleScheduleFilterChange() {
      this.form.scheduleId = undefined
      this.getScheduleOptions()
    },
    handleScheduleVisible(visible) {
      if (visible) {
        this.getScheduleOptions()
      }
    },
    handleScheduleChange(value) {
      const schedule = this.formScheduleOptions.find(item => item.scheduleId === value)
      if (schedule) {
        this.form.departmentId = schedule.departmentId
        this.form.doctorId = schedule.doctorId
        this.form.scheduleDate = schedule.scheduleDate
        this.form.timeSlot = schedule.timeSlot
        this.formDoctorOptions = [schedule]
      }
    },
    formatTimeSlot(value) {
      const item = this.timeSlotOptions.find(option => option.value === value)
      return item ? item.label : "-"
    },
    formatSourceStatus(value) {
      const item = this.sourceStatusOptions.find(option => option.value === value)
      return item ? item.label : "-"
    },
    formatSourceStatusType(value) {
      const item = this.sourceStatusOptions.find(option => option.value === value)
      return item ? item.type : "info"
    },
    formatScheduleLabel(item) {
      return item.departmentName + " / " + item.doctorName + " / " + this.parseTime(item.scheduleDate, "{y}-{m}-{d}") + " / " + this.formatTimeSlot(item.timeSlot) + " / " + (item.location || "-")
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        sourceId: undefined,
        scheduleId: undefined,
        departmentId: undefined,
        doctorId: undefined,
        scheduleDate: undefined,
        timeSlot: undefined,
        totalNum: 30,
        remainNum: 30,
        status: "0",
        remark: undefined
      }
      this.formDoctorOptions = []
      this.formScheduleOptions = []
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
      this.isEdit = false
      this.open = true
      this.title = "添加号源"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.sourceId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const sourceId = row.sourceId || this.ids
      getSource(sourceId).then(response => {
        this.form = response.data
        this.formDoctorOptions = [this.form]
        this.formScheduleOptions = [this.form]
        this.isEdit = true
        this.open = true
        this.title = "修改号源"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.sourceId != undefined) {
            updateSource(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSource(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const sourceIds = row.sourceId || this.ids
      this.$modal.confirm('是否确认删除号源编号为"' + sourceIds + '"的数据项？').then(function() {
        return delSource(sourceIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('hospital/source/export', {
        ...this.queryParams
      }, `source_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
