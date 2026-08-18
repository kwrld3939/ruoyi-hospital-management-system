<template>
  <div class="app-container medical-record" v-loading="loading">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px" class="record-filter">
      <el-form-item label="患者" prop="patientId">
        <el-select
          v-model="queryParams.patientId"
          placeholder="输入姓名/编码/手机号/身份证号搜索患者"
          clearable
          filterable
          remote
          reserve-keyword
          :remote-method="remoteQueryPatients"
          :loading="patientLoading"
          style="width: 360px"
          @change="handlePatientChange"
        >
          <el-option
            v-for="item in patientOptions"
            :key="item.patientId"
            :label="formatPatientLabel(item)"
            :value="item.patientId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleLoad">查看病历</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <template v-if="patientInfo.patientId">
      <div class="patient-strip">
        <div>
          <div class="patient-name">{{ patientInfo.patientName || '-' }}</div>
          <div class="patient-meta">
            <span>编码：{{ patientInfo.patientCode || '-' }}</span>
            <span>性别：{{ genderText(patientInfo.gender) }}</span>
            <span>手机号：{{ patientInfo.phone || '-' }}</span>
            <span>身份证：{{ patientInfo.idCard || '-' }}</span>
          </div>
        </div>
        <el-button icon="el-icon-refresh" size="mini" @click="loadMedicalRecord(queryParams.patientId)">刷新</el-button>
      </div>

      <el-row :gutter="16" class="summary-row">
        <el-col v-for="item in summaryCards" :key="item.key" :xs="12" :sm="12" :md="6">
          <div class="summary-card" :class="'summary-card--' + item.type">
            <div class="summary-icon"><i :class="item.icon"></i></div>
            <div>
              <div class="summary-label">{{ item.label }}</div>
              <div class="summary-value">{{ item.value }}</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <div class="record-grid">
        <div class="record-panel">
          <div class="panel-head">
            <span>最近就诊摘要</span>
            <span class="panel-count">共 {{ recordSummary.visitCount }} 次</span>
          </div>
          <el-table :data="recentVisitList" size="small" empty-text="暂无就诊记录">
            <el-table-column label="就诊时间" prop="visitTime" width="155">
              <template slot-scope="scope">{{ parseTime(scope.row.visitTime) }}</template>
            </el-table-column>
            <el-table-column label="科室" prop="departmentName" min-width="100" show-overflow-tooltip />
            <el-table-column label="医生" prop="doctorName" min-width="90" show-overflow-tooltip />
            <el-table-column label="诊断摘要" prop="diagnosis" min-width="180" show-overflow-tooltip />
            <el-table-column label="处理意见" prop="treatmentAdvice" min-width="180" show-overflow-tooltip />
            <el-table-column label="状态" width="80" align="center">
              <template slot-scope="scope">
                <el-tag size="mini" :type="scope.row.status === '0' ? 'success' : 'info'">
                  {{ visitStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="record-panel">
          <div class="panel-head">
            <span>最近预约概况</span>
            <span class="panel-count">共 {{ recordSummary.registrationCount }} 次</span>
          </div>
          <el-table :data="recentRegistrationList" size="small" empty-text="暂无预约记录">
            <el-table-column label="预约日期" prop="visitDate" width="120">
              <template slot-scope="scope">{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</template>
            </el-table-column>
            <el-table-column label="时段" width="80">
              <template slot-scope="scope">{{ timeSlotText(scope.row.timeSlot) }}</template>
            </el-table-column>
            <el-table-column label="科室" prop="departmentName" min-width="100" show-overflow-tooltip />
            <el-table-column label="医生" prop="doctorName" min-width="90" show-overflow-tooltip />
            <el-table-column label="状态" width="86" align="center">
              <template slot-scope="scope">
                <el-tag size="mini" :type="registrationStatusType(scope.row.status)">
                  {{ registrationStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </template>

    <el-empty v-else description="请选择患者查看病历" />
  </div>
</template>

<script>
import { listPatient, getPatient } from "@/api/hospital/patient"
import { listRegistration } from "@/api/hospital/registration"
import { listVisit } from "@/api/hospital/visit"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "HospitalMedicalRecord",
  data() {
    return {
      loading: false,
      patientLoading: false,
      patientTimer: null,
      patientOptions: [],
      queryParams: {
        patientId: this.$route.query.patientId ? Number(this.$route.query.patientId) : undefined
      },
      patientInfo: {},
      registrationList: [],
      visitList: [],
      recordSummary: {
        registrationCount: 0,
        visitCount: 0,
        cancelCount: 0,
        noShowCount: 0
      }
    }
  },
  computed: {
    summaryCards() {
      return [
        { key: "registration", label: "预约次数", value: this.recordSummary.registrationCount || 0, icon: "el-icon-tickets", type: "blue" },
        { key: "visit", label: "就诊次数", value: this.recordSummary.visitCount || 0, icon: "el-icon-first-aid-kit", type: "green" },
        { key: "cancel", label: "取消次数", value: this.recordSummary.cancelCount || 0, icon: "el-icon-circle-close", type: "amber" },
        { key: "noshow", label: "爽约次数", value: this.recordSummary.noShowCount || 0, icon: "el-icon-warning-outline", type: "red" }
      ]
    },
    recentVisitList() {
      return this.sortByDate(this.visitList, "visitTime").slice(0, 5)
    },
    recentRegistrationList() {
      return this.sortByDate(this.registrationList, "visitDate").slice(0, 5)
    }
  },
  created() {
    this.applyRouteQuery()
  },
  activated() {
    this.applyRouteQuery()
  },
  watch: {
    "$route.query.patientId"() {
      this.applyRouteQuery()
    }
  },
  methods: {
    parseTime,
    applyRouteQuery() {
      const patientId = this.$route.query.patientId ? Number(this.$route.query.patientId) : undefined
      if (patientId && patientId !== this.queryParams.patientId) {
        this.queryParams.patientId = patientId
      }
      if (this.queryParams.patientId && this.queryParams.patientId !== this.patientInfo.patientId) {
        this.loadMedicalRecord(this.queryParams.patientId)
      }
    },
    remoteQueryPatients(keyword) {
      clearTimeout(this.patientTimer)
      this.patientTimer = setTimeout(() => {
        if (!keyword || !keyword.trim()) {
          this.patientOptions = this.patientInfo.patientId ? [this.patientInfo] : []
          return
        }
        this.patientLoading = true
        listPatient({ pageNum: 1, pageSize: 20, patientKeyword: keyword.trim() }).then(response => {
          this.patientOptions = response.rows || []
        }).finally(() => {
          this.patientLoading = false
        })
      }, 300)
    },
    handlePatientChange(value) {
      if (!value) {
        this.resetRecord()
        return
      }
      this.loadMedicalRecord(value)
    },
    handleLoad() {
      if (!this.queryParams.patientId) {
        this.$modal.msgWarning("请先选择患者")
        return
      }
      this.loadMedicalRecord(this.queryParams.patientId)
    },
    loadMedicalRecord(patientId) {
      if (!patientId) {
        this.resetRecord()
        return
      }
      this.loading = true
      Promise.all([
        getPatient(patientId),
        listRegistration({ pageNum: 1, pageSize: 1000, patientId: patientId }),
        listVisit({ pageNum: 1, pageSize: 1000, patientId: patientId })
      ]).then(([patientResponse, registrationResponse, visitResponse]) => {
        const patient = patientResponse.data || {}
        this.patientInfo = patient
        this.patientOptions = patient.patientId ? [patient] : []
        this.registrationList = registrationResponse.rows || []
        this.visitList = visitResponse.rows || []
        this.recordSummary = {
          registrationCount: this.registrationList.length,
          visitCount: this.visitList.length,
          cancelCount: this.registrationList.filter(item => item.status === "1").length,
          noShowCount: this.registrationList.filter(item => item.status === "3").length
        }
      }).finally(() => {
        this.loading = false
      })
    },
    resetQuery() {
      this.queryParams.patientId = undefined
      this.patientOptions = []
      this.resetRecord()
    },
    resetRecord() {
      this.patientInfo = {}
      this.registrationList = []
      this.visitList = []
      this.recordSummary = {
        registrationCount: 0,
        visitCount: 0,
        cancelCount: 0,
        noShowCount: 0
      }
    },
    formatPatientLabel(item) {
      return (item.patientName || "-") + "（" + (item.patientCode || "-") + "） / " + (item.phone || "-")
    },
    genderText(value) {
      if (value === "0") return "男"
      if (value === "1") return "女"
      return "-"
    },
    timeSlotText(value) {
      if (value === "1") return "上午"
      if (value === "2") return "下午"
      if (value === "3") return "晚上"
      return "-"
    },
    registrationStatusText(value) {
      if (value === "0") return "已预约"
      if (value === "1") return "已取消"
      if (value === "2") return "已就诊"
      if (value === "3") return "爽约"
      return "-"
    },
    registrationStatusType(value) {
      if (value === "0") return "warning"
      if (value === "1") return "info"
      if (value === "2") return "success"
      if (value === "3") return "danger"
      return ""
    },
    visitStatusText(value) {
      if (value === "0") return "已就诊"
      if (value === "1") return "作废"
      return "-"
    },
    sortByDate(list, field) {
      return [...(list || [])].sort((a, b) => {
        const left = a[field] ? new Date(a[field]).getTime() : 0
        const right = b[field] ? new Date(b[field]).getTime() : 0
        return right - left
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.medical-record {
  background: #f5f7fb;
}

.record-filter,
.patient-strip,
.record-panel {
  background: #fff;
  border: 1px solid #e5e9f2;
  border-radius: 6px;
}

.record-filter {
  padding: 16px 16px 0;
  margin-bottom: 14px;
}

.patient-strip {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  padding: 14px 16px;
  margin-bottom: 14px;
}

.patient-name {
  color: #1f2d3d;
  font-size: 18px;
  font-weight: 600;
  line-height: 28px;
}

.patient-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 16px;
  color: #7a8799;
  font-size: 13px;
  line-height: 22px;
}

.summary-row {
  margin-bottom: 14px;
}

.summary-card {
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 84px;
  padding: 16px;
  margin-bottom: 14px;
  background: #fff;
  border: 1px solid #e5e9f2;
  border-radius: 6px;
}

.summary-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  border-radius: 50%;
  font-size: 22px;
}

.summary-label {
  color: #7a8799;
  font-size: 13px;
  line-height: 20px;
}

.summary-value {
  color: #1f2d3d;
  font-size: 26px;
  font-weight: 700;
  line-height: 34px;
}

.summary-card--blue .summary-icon {
  color: #2477f3;
  background: #eaf2ff;
}

.summary-card--green .summary-icon {
  color: #17a673;
  background: #e9f8f2;
}

.summary-card--amber .summary-icon {
  color: #b7791f;
  background: #fff5df;
}

.summary-card--red .summary-icon {
  color: #f56c6c;
  background: #fff0f0;
}

.record-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.25fr) minmax(360px, 0.75fr);
  gap: 14px;
}

.record-panel {
  min-width: 0;
  padding: 14px 16px 16px;
}

.panel-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  color: #1f2d3d;
  font-size: 15px;
  font-weight: 600;
  line-height: 22px;
}

.panel-count {
  color: #7a8799;
  font-size: 13px;
  font-weight: 400;
}

@media (max-width: 1200px) {
  .record-grid {
    grid-template-columns: 1fr;
  }
}
</style>
