<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="76px">
      <el-form-item label="挂号单号" prop="registrationNo">
        <el-input
          v-model="queryParams.registrationNo"
          placeholder="请输入挂号单号"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="患者关键词" prop="patientKeyword">
        <el-input
          v-model="queryParams.patientKeyword"
          placeholder="姓名/编码/手机号/身份证号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="科室" prop="departmentId">
        <el-select v-model="queryParams.departmentId" placeholder="请选择科室" clearable filterable style="width: 180px">
          <el-option
            v-for="item in deptOptions"
            :key="item.departmentId"
            :label="item.departmentName"
            :value="item.departmentId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="医生" prop="doctorId">
        <el-select v-model="queryParams.doctorId" placeholder="输入姓名或编码搜索" clearable filterable remote reserve-keyword
                   :remote-method="remoteQueryDoctors" :loading="queryDoctorLoading" style="width: 220px">
          <el-option
            v-for="item in queryDoctorOptions"
            :key="item.doctorId"
            :label="item.doctorName + '（' + item.doctorCode + '）'"
            :value="item.doctorId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="visitDate">
        <el-date-picker v-model="queryParams.visitDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" style="width: 160px" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="预约状态" clearable style="width: 140px">
          <el-option label="已预约" value="0" />
          <el-option label="已取消" value="1" />
          <el-option label="已就诊" value="2" />
          <el-option label="爽约" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['hospital:registration:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['hospital:registration:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['hospital:registration:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="registrationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="挂号单号" align="center" prop="registrationNo" width="160" />
      <el-table-column label="患者" align="center" prop="patientName" :show-overflow-tooltip="true" />
      <el-table-column label="手机号" align="center" prop="patientPhone" width="130" />
      <el-table-column label="科室" align="center" prop="departmentName" :show-overflow-tooltip="true" />
      <el-table-column label="医生" align="center" prop="doctorName" :show-overflow-tooltip="true" />
      <el-table-column label="就诊日期" align="center" prop="visitDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间段" align="center" prop="timeSlot" width="90">
        <template slot-scope="scope">
          <span>{{ formatTimeSlot(scope.row.timeSlot) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag :type="formatStatusType(scope.row.status)">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="挂号时间" align="center" prop="registrationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registrationTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleUpdate(scope.row)" v-hasPermi="['hospital:registration:edit']">备注</el-button>
          <el-button size="mini" type="text" icon="el-icon-close" @click="handleCancel(scope.row)" v-hasPermi="['hospital:registration:cancel']">取消</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['hospital:registration:remove']">删除</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="820px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="患者" prop="patientId">
              <el-select v-model="form.patientId" placeholder="输入姓名或编码搜索" clearable filterable remote reserve-keyword
                         :remote-method="remoteFormPatients" :loading="formPatientLoading" style="width: 100%" @change="handlePatientChange">
                <el-option
                  v-for="item in formPatientOptions"
                  :key="item.patientId"
                  :label="item.patientName + '（' + item.patientCode + '）'"
                  :value="item.patientId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="号源" prop="sourceId">
              <el-select v-model="form.sourceId" placeholder="选择可预约号源" filterable clearable :loading="sourceLoading" style="width: 100%" @visible-change="handleSourceVisible" @change="handleSourceChange">
                <el-option
                  v-for="item in sourceOptions"
                  :key="item.sourceId"
                  :label="formatSourceLabel(item)"
                  :value="item.sourceId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="patientName">
              <el-input v-model="form.patientName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="患者电话" prop="patientPhone">
              <el-input v-model="form.patientPhone" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="科室" prop="departmentName">
              <el-input v-model="form.departmentName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医生" prop="doctorName">
              <el-input v-model="form.doctorName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="就诊日期" prop="visitDate">
              <el-date-picker v-model="form.visitDate" type="date" value-format="yyyy-MM-dd" placeholder="自动带出" style="width: 100%" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="时间段" prop="timeSlot">
              <el-select v-model="form.timeSlot" disabled style="width: 100%">
                <el-option v-for="item in timeSlotOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
import { listRegistration, getRegistration, addRegistration, updateRegistration, cancelRegistration, delRegistration } from "@/api/hospital/registration"
import { listPatient } from "@/api/hospital/patient"
import { listSource } from "@/api/hospital/source"
import { listDepartment } from "@/api/hospital/department"
import { listDoctor } from "@/api/hospital/doctor"

export default {
  name: "HospitalRegistration",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      registrationList: [],
      deptOptions: [],
      queryDoctorOptions: [],
      formPatientOptions: [],
      sourceOptions: [],
      queryDoctorLoading: false,
      formPatientLoading: false,
      sourceLoading: false,
      queryDoctorTimer: null,
      formPatientTimer: null,
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
        registrationNo: undefined,
        patientKeyword: undefined,
        departmentId: undefined,
        doctorId: undefined,
        visitDate: undefined,
        status: undefined
      },
      form: {},
      rules: {
        patientId: [
          { required: true, message: "患者不能为空", trigger: "change" }
        ],
        sourceId: [
          { required: true, message: "号源不能为空", trigger: "change" }
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
      listRegistration(this.queryParams).then(response => {
        this.registrationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getDeptOptions() {
      listDepartment({ pageNum: 1, pageSize: 1000, status: "0" }).then(response => {
        this.deptOptions = response.rows || []
      })
    },
    remoteQueryDoctors(keyword) {
      clearTimeout(this.queryDoctorTimer)
      this.queryDoctorTimer = setTimeout(() => {
        if (!keyword || !keyword.trim()) {
          this.queryDoctorOptions = []
          return
        }
        const query = { pageNum: 1, pageSize: 20, status: "0", doctorKeyword: keyword.trim() }
        if (this.queryParams.departmentId) {
          query.departmentId = this.queryParams.departmentId
        }
        this.queryDoctorLoading = true
        listDoctor(query).then(response => {
          this.queryDoctorOptions = response.rows || []
          this.queryDoctorLoading = false
        }).catch(() => {
          this.queryDoctorLoading = false
        })
      }, 300)
    },
    remoteFormPatients(keyword) {
      clearTimeout(this.formPatientTimer)
      this.formPatientTimer = setTimeout(() => {
        if (!keyword || !keyword.trim()) {
          this.formPatientOptions = []
          return
        }
        this.formPatientLoading = true
        listPatient({ pageNum: 1, pageSize: 20, status: "0", patientKeyword: keyword.trim() }).then(response => {
          this.formPatientOptions = response.rows || []
          this.formPatientLoading = false
        }).catch(() => {
          this.formPatientLoading = false
        })
      }, 300)
    },
    getSourceOptions() {
      const query = { pageNum: 1, pageSize: 20, status: "0" }
      if (this.form.departmentId) {
        query.departmentId = this.form.departmentId
      }
      if (this.form.doctorId) {
        query.doctorId = this.form.doctorId
      }
      if (this.form.visitDate) {
        query.scheduleDate = this.form.visitDate
      }
      if (this.form.timeSlot) {
        query.timeSlot = this.form.timeSlot
      }
      this.sourceLoading = true
      listSource(query).then(response => {
        this.sourceOptions = response.rows || []
        this.sourceLoading = false
      }).catch(() => {
        this.sourceLoading = false
      })
    },
    handleSourceVisible(visible) {
      if (visible) {
        this.getSourceOptions()
      }
    },
    handlePatientChange(value) {
      const patient = this.formPatientOptions.find(item => item.patientId === value)
      if (patient) {
        this.form.patientName = patient.patientName
        this.form.patientCode = patient.patientCode
        this.form.patientPhone = patient.phone
      }
    },
    handleSourceChange(value) {
      const source = this.sourceOptions.find(item => item.sourceId === value)
      if (source) {
        this.form.sourceId = source.sourceId
        this.form.scheduleId = source.scheduleId
        this.form.departmentId = source.departmentId
        this.form.departmentName = source.departmentName
        this.form.doctorId = source.doctorId
        this.form.doctorName = source.doctorName
        this.form.doctorCode = source.doctorCode
        this.form.visitDate = source.scheduleDate
        this.form.timeSlot = source.timeSlot
        this.form.location = source.location
      }
    },
    formatTimeSlot(value) {
      const item = this.timeSlotOptions.find(option => option.value === value)
      return item ? item.label : "-"
    },
    formatStatus(value) {
      const map = { "0": "已预约", "1": "已取消", "2": "已就诊", "3": "爽约" }
      return map[value] || "-"
    },
    formatStatusType(value) {
      const map = { "0": "success", "1": "info", "2": "warning", "3": "danger" }
      return map[value] || "info"
    },
    formatSourceLabel(item) {
      return item.departmentName + " / " + item.doctorName + " / " + this.parseTime(item.scheduleDate, "{y}-{m}-{d}") + " / " + this.formatTimeSlot(item.timeSlot) + " / 剩余" + item.remainNum
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        registrationId: undefined,
        registrationNo: undefined,
        patientId: undefined,
        patientName: undefined,
        patientCode: undefined,
        patientPhone: undefined,
        sourceId: undefined,
        scheduleId: undefined,
        departmentId: undefined,
        departmentName: undefined,
        doctorId: undefined,
        doctorName: undefined,
        doctorCode: undefined,
        visitDate: undefined,
        timeSlot: undefined,
        location: undefined,
        status: "0",
        remark: undefined
      }
      this.formPatientOptions = []
      this.sourceOptions = []
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
      this.title = "添加预约挂号"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.registrationId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const registrationId = row.registrationId || this.ids
      getRegistration(registrationId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改预约挂号"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.registrationId != undefined) {
            updateRegistration(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRegistration(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleCancel(row) {
      this.$modal.confirm('是否确认取消挂号单号为"' + row.registrationNo + '"的预约？').then(() => {
        return cancelRegistration({ registrationId: row.registrationId })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("取消成功")
      }).catch(() => {})
    },
    handleDelete(row) {
      const registrationIds = row.registrationId || this.ids
      this.$modal.confirm('是否确认删除挂号编号为"' + registrationIds + '"的数据项？').then(function() {
        return delRegistration(registrationIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('hospital/registration/export', { ...this.queryParams }, `registration_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
