<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="76px">
      <el-form-item label="挂号单号" prop="registrationNo">
        <el-input v-model="queryParams.registrationNo" placeholder="请输入挂号单号" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="患者关键词" prop="patientKeyword">
        <el-input v-model="queryParams.patientKeyword" placeholder="姓名/编码/手机号/身份证号" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="科室" prop="departmentId">
        <el-select v-model="queryParams.departmentId" placeholder="请选择科室" clearable filterable style="width: 180px">
          <el-option v-for="item in deptOptions" :key="item.departmentId" :label="item.departmentName" :value="item.departmentId" />
        </el-select>
      </el-form-item>
      <el-form-item label="医生" prop="doctorId">
        <el-select v-model="queryParams.doctorId" placeholder="输入姓名或编码搜索" clearable filterable remote reserve-keyword
                   :remote-method="remoteQueryDoctors" :loading="queryDoctorLoading" style="width: 220px">
          <el-option v-for="item in queryDoctorOptions" :key="item.doctorId" :label="item.doctorName + '（' + item.doctorCode + '）'" :value="item.doctorId" />
        </el-select>
      </el-form-item>
      <el-form-item label="就诊日期" prop="visitDate">
        <el-date-picker v-model="queryParams.visitDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" style="width: 160px" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="就诊状态" clearable style="width: 120px">
          <el-option label="已就诊" value="0" />
          <el-option label="作废" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['hospital:visit:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['hospital:visit:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['hospital:visit:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['hospital:visit:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="visitList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="挂号单号" align="center" prop="registrationNo" width="160" />
      <el-table-column label="患者" align="center" prop="patientName" :show-overflow-tooltip="true" />
      <el-table-column label="科室" align="center" prop="departmentName" :show-overflow-tooltip="true" />
      <el-table-column label="医生" align="center" prop="doctorName" :show-overflow-tooltip="true" />
      <el-table-column label="主诉" align="center" prop="chiefComplaint" :show-overflow-tooltip="true" />
      <el-table-column label="初步诊断" align="center" prop="diagnosis" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">{{ scope.row.status === '0' ? '已就诊' : '作废' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="就诊时间" align="center" prop="visitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['hospital:visit:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['hospital:visit:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="820px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="挂号记录" prop="registrationId">
          <el-select v-model="form.registrationId" placeholder="输入患者姓名/编码/手机号搜索" clearable filterable remote reserve-keyword
                     :remote-method="remoteRegistrations" :loading="registrationLoading" :disabled="isEdit" style="width: 100%" @change="handleRegistrationChange">
            <el-option v-for="item in registrationOptions" :key="item.registrationId" :label="formatRegistrationLabel(item)" :value="item.registrationId" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="患者" prop="patientName">
              <el-input v-model="form.patientName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="patientPhone">
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
        <el-form-item label="主诉" prop="chiefComplaint">
          <el-input v-model="form.chiefComplaint" type="textarea" placeholder="请输入主诉" />
        </el-form-item>
        <el-form-item label="初步诊断" prop="diagnosis">
          <el-input v-model="form.diagnosis" type="textarea" placeholder="请输入初步诊断" />
        </el-form-item>
        <el-form-item label="处理意见" prop="treatmentAdvice">
          <el-input v-model="form.treatmentAdvice" type="textarea" placeholder="请输入处理意见" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">已就诊</el-radio>
            <el-radio label="1">作废</el-radio>
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
import { listVisit, getVisit, addVisit, updateVisit, delVisit } from "@/api/hospital/visit"
import { listRegistration, getRegistration } from "@/api/hospital/registration"
import { listDepartment } from "@/api/hospital/department"
import { listDoctor } from "@/api/hospital/doctor"

export default {
  name: "HospitalVisit",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      visitList: [],
      deptOptions: [],
      queryDoctorOptions: [],
      registrationOptions: [],
      queryDoctorLoading: false,
      registrationLoading: false,
      queryDoctorTimer: null,
      registrationTimer: null,
      fromWorkbench: false,
      workbenchQuery: {},
      handledRegistrationId: undefined,
      title: "",
      open: false,
      isEdit: false,
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
        registrationId: [
          { required: true, message: "挂号记录不能为空", trigger: "change" }
        ],
        chiefComplaint: [
          { required: true, message: "主诉不能为空", trigger: "blur" }
        ],
        diagnosis: [
          { required: true, message: "初步诊断不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getDeptOptions()
    this.getList()
    this.openFromWorkbench()
  },
  activated() {
    this.openFromWorkbench()
  },
  watch: {
    "$route.query.registrationId"() {
      this.openFromWorkbench()
    }
  },
  methods: {
    getList() {
      this.loading = true
      listVisit(this.queryParams).then(response => {
        this.visitList = response.rows
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
    remoteRegistrations(keyword) {
      clearTimeout(this.registrationTimer)
      this.registrationTimer = setTimeout(() => {
        if (!keyword || !keyword.trim()) {
          this.registrationOptions = []
          return
        }
        this.registrationLoading = true
        listRegistration({ pageNum: 1, pageSize: 20, status: "0", patientKeyword: keyword.trim() }).then(response => {
          this.registrationOptions = response.rows || []
          this.registrationLoading = false
        }).catch(() => {
          this.registrationLoading = false
        })
      }, 300)
    },
    handleRegistrationChange(value) {
      const registration = this.registrationOptions.find(item => item.registrationId === value)
      if (registration) {
        this.fillRegistrationForm(registration)
      }
    },
    fillRegistrationForm(registration) {
      this.form.registrationId = registration.registrationId
      this.form.registrationNo = registration.registrationNo
      this.form.patientId = registration.patientId
      this.form.patientName = registration.patientName
      this.form.patientCode = registration.patientCode
      this.form.patientPhone = registration.patientPhone
      this.form.departmentId = registration.departmentId
      this.form.departmentName = registration.departmentName
      this.form.doctorId = registration.doctorId
      this.form.doctorName = registration.doctorName
      this.form.doctorCode = registration.doctorCode
    },
    openFromWorkbench() {
      const registrationId = this.$route.query.registrationId
      if (!registrationId || registrationId === this.handledRegistrationId) {
        return
      }
      this.handledRegistrationId = registrationId
      this.reset()
      this.fromWorkbench = this.$route.query.fromWorkbench === "1"
      this.workbenchQuery = {
        doctorId: this.$route.query.doctorId,
        visitDate: this.$route.query.visitDate
      }
      this.isEdit = false
      this.title = "添加就诊记录"
      getRegistration(registrationId).then(response => {
        const registration = response.data
        if (!registration || registration.status !== "0") {
          this.$modal.msgWarning("只有已预约状态的挂号记录可以接诊")
          return
        }
        this.registrationOptions = [registration]
        this.open = true
        this.fillRegistrationForm(registration)
      })
    },
    formatRegistrationLabel(item) {
      return item.registrationNo + " / " + item.patientName + " / " + item.departmentName + " / " + item.doctorName
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        visitId: undefined,
        registrationId: undefined,
        registrationNo: undefined,
        patientId: undefined,
        patientName: undefined,
        patientCode: undefined,
        patientPhone: undefined,
        departmentId: undefined,
        departmentName: undefined,
        doctorId: undefined,
        doctorName: undefined,
        doctorCode: undefined,
        chiefComplaint: undefined,
        diagnosis: undefined,
        treatmentAdvice: undefined,
        status: "0",
        remark: undefined
      }
      this.registrationOptions = []
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
      this.title = "添加就诊记录"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.visitId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const visitId = row.visitId || this.ids
      getVisit(visitId).then(response => {
        this.form = response.data
        this.registrationOptions = [this.form]
        this.isEdit = true
        this.open = true
        this.title = "修改就诊记录"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.visitId != undefined) {
            updateVisit(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addVisit(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              if (this.fromWorkbench) {
                this.$router.push({ path: "/hospital/doctorWorkbench", query: this.workbenchQuery })
              } else {
                this.getList()
              }
            })
          }
        }
      })
    },
    handleDelete(row) {
      const visitIds = row.visitId || this.ids
      this.$modal.confirm('是否确认删除就诊记录编号为"' + visitIds + '"的数据项？').then(function() {
        return delVisit(visitIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('hospital/visit/export', { ...this.queryParams }, `visit_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
