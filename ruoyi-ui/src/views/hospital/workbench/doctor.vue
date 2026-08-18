<template>
  <div class="app-container doctor-workbench" v-loading="loading">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="72px" class="workbench-filter">
      <el-form-item label="医生" prop="doctorId">
        <el-select v-model="queryParams.doctorId" placeholder="输入姓名或编码搜索" clearable filterable remote reserve-keyword
                   :remote-method="remoteDoctors" :loading="doctorLoading" style="width: 260px" @change="handleQuery">
          <el-option v-for="item in doctorOptions" :key="item.doctorId" :label="formatDoctorLabel(item)" :value="item.doctorId" />
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="visitDate">
        <el-date-picker v-model="queryParams.visitDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" style="width: 160px" @change="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="doctor-strip">
      <div>
        <div class="doctor-name">{{ workbench.doctorName || '请选择医生' }}</div>
        <div class="doctor-meta">
          <span>{{ workbench.departmentName || '未选择科室' }}</span>
          <span>{{ workbench.visitDate || queryParams.visitDate }}</span>
        </div>
      </div>
      <el-button icon="el-icon-refresh" size="mini" @click="getWorkbench">刷新</el-button>
    </div>

    <el-row :gutter="16" class="metric-row">
      <el-col v-for="item in metricCards" :key="item.key" :xs="12" :sm="12" :md="6" :lg="4">
        <div class="metric-card" :class="'metric-card--' + item.type">
          <div class="metric-icon"><i :class="item.icon"></i></div>
          <div>
            <div class="metric-label">{{ item.label }}</div>
            <div class="metric-value">{{ item.value }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="16">
        <div class="workbench-panel">
          <div class="panel-header">
            <div>
              <div class="panel-title">今日预约列表</div>
              <div class="panel-subtitle">按就诊时间段和挂号时间排序</div>
            </div>
          </div>
          <el-table :data="workbench.registrationList" size="small" height="360">
            <el-table-column label="挂号单号" prop="registrationNo" min-width="150" show-overflow-tooltip />
            <el-table-column label="患者" prop="patientName" min-width="90" show-overflow-tooltip />
            <el-table-column label="手机号" prop="patientPhone" min-width="120" show-overflow-tooltip />
            <el-table-column label="时段" width="76">
              <template slot-scope="scope">{{ timeSlotText(scope.row.timeSlot) }}</template>
            </el-table-column>
            <el-table-column label="地点" prop="location" min-width="100" show-overflow-tooltip />
            <el-table-column label="状态" width="90">
              <template slot-scope="scope">
                <el-tag size="mini" :type="registrationStatusType(scope.row.status)">
                  {{ registrationStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <el-col :xs="24" :lg="8">
        <div class="workbench-panel">
          <div class="panel-header">
            <div>
              <div class="panel-title">今日排班号源</div>
              <div class="panel-subtitle">医生当天出诊地点和号源余量</div>
            </div>
          </div>
          <el-table :data="workbench.scheduleList" size="small" height="170">
            <el-table-column label="时段" width="72">
              <template slot-scope="scope">{{ timeSlotText(scope.row.timeSlot) }}</template>
            </el-table-column>
            <el-table-column label="地点" prop="location" min-width="95" show-overflow-tooltip />
            <el-table-column label="号源" width="92" align="center">
              <template slot-scope="scope">
                <el-tag size="mini" :type="sourceStatusType(scope.row)">
                  {{ scope.row.remainNum || 0 }}/{{ scope.row.totalNum || 0 }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="workbench-panel">
          <div class="panel-header">
            <div>
              <div class="panel-title">待接诊患者</div>
              <div class="panel-subtitle">已预约但尚未生成就诊记录</div>
            </div>
          </div>
          <el-table :data="workbench.pendingList" size="small" height="360">
            <el-table-column label="患者" prop="patientName" min-width="90" show-overflow-tooltip />
            <el-table-column label="手机号" prop="patientPhone" min-width="120" show-overflow-tooltip />
            <el-table-column label="时段" width="76">
              <template slot-scope="scope">{{ timeSlotText(scope.row.timeSlot) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="86" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-first-aid-kit" @click="goVisit(scope.row)">接诊</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>

    <div class="workbench-panel">
      <div class="panel-header">
        <div>
          <div class="panel-title">最近就诊记录</div>
          <div class="panel-subtitle">当前医生最近维护的轻量接诊记录</div>
        </div>
      </div>
      <el-table :data="workbench.recentVisitList" size="small">
        <el-table-column label="挂号单号" prop="registrationNo" min-width="150" show-overflow-tooltip />
        <el-table-column label="患者" prop="patientName" min-width="90" show-overflow-tooltip />
        <el-table-column label="手机号" prop="patientPhone" min-width="120" show-overflow-tooltip />
        <el-table-column label="主诉" prop="chiefComplaint" min-width="180" show-overflow-tooltip />
        <el-table-column label="初步诊断" prop="diagnosis" min-width="180" show-overflow-tooltip />
        <el-table-column label="就诊时间" prop="visitTime" width="170" />
      </el-table>
    </div>
  </div>
</template>

<script>
import { listDoctor } from "@/api/hospital/doctor"
import { getDoctorWorkbench } from "@/api/hospital/workbench"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "HospitalDoctorWorkbench",
  data() {
    return {
      loading: false,
      doctorLoading: false,
      doctorTimer: null,
      doctorOptions: [],
      queryParams: {
        doctorId: this.$route.query.doctorId ? Number(this.$route.query.doctorId) : undefined,
        visitDate: this.$route.query.visitDate || parseTime(new Date(), "{y}-{m}-{d}")
      },
      workbench: {
        doctorId: undefined,
        doctorName: undefined,
        departmentName: undefined,
        visitDate: undefined,
        todayRegistrationCount: 0,
        pendingCount: 0,
        visitedCount: 0,
        cancelCount: 0,
        noShowCount: 0,
        registrationList: [],
        pendingList: [],
        scheduleList: [],
        recentVisitList: []
      }
    }
  },
  computed: {
    metricCards() {
      return [
        { key: "total", label: "今日预约", value: this.workbench.todayRegistrationCount || 0, icon: "el-icon-tickets", type: "blue" },
        { key: "pending", label: "待接诊", value: this.workbench.pendingCount || 0, icon: "el-icon-time", type: "amber" },
        { key: "visited", label: "已接诊", value: this.workbench.visitedCount || 0, icon: "el-icon-circle-check", type: "green" },
        { key: "cancel", label: "已取消", value: this.workbench.cancelCount || 0, icon: "el-icon-circle-close", type: "red" },
        { key: "noshow", label: "爽约", value: this.workbench.noShowCount || 0, icon: "el-icon-warning-outline", type: "gray" }
      ]
    }
  },
  created() {
    this.loadDefaultDoctors()
  },
  activated() {
    this.applyRouteQuery()
  },
  methods: {
    applyRouteQuery() {
      if (this.$route.query.doctorId) {
        this.queryParams.doctorId = Number(this.$route.query.doctorId)
      }
      if (this.$route.query.visitDate) {
        this.queryParams.visitDate = this.$route.query.visitDate
      }
      if (this.queryParams.doctorId) {
        this.getWorkbench()
      }
    },
    loadDefaultDoctors() {
      this.doctorLoading = true
      listDoctor({ pageNum: 1, pageSize: 20, status: "0" }).then(response => {
        this.doctorOptions = response.rows || []
        if (!this.queryParams.doctorId && this.doctorOptions.length > 0) {
          this.queryParams.doctorId = this.doctorOptions[0].doctorId
        }
        this.getWorkbench()
      }).finally(() => {
        this.doctorLoading = false
      })
    },
    remoteDoctors(keyword) {
      clearTimeout(this.doctorTimer)
      this.doctorTimer = setTimeout(() => {
        const query = { pageNum: 1, pageSize: 20, status: "0" }
        if (keyword && keyword.trim()) {
          query.doctorKeyword = keyword.trim()
        }
        this.doctorLoading = true
        listDoctor(query).then(response => {
          this.doctorOptions = response.rows || []
        }).finally(() => {
          this.doctorLoading = false
        })
      }, 300)
    },
    getWorkbench() {
      if (!this.queryParams.doctorId) {
        this.resetWorkbench()
        return
      }
      this.loading = true
      getDoctorWorkbench(this.queryParams).then(response => {
        this.workbench = Object.assign({}, this.workbench, response.data || {})
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.getWorkbench()
    },
    resetQuery() {
      this.queryParams.visitDate = parseTime(new Date(), "{y}-{m}-{d}")
      this.queryParams.doctorId = this.doctorOptions.length > 0 ? this.doctorOptions[0].doctorId : undefined
      this.getWorkbench()
    },
    resetWorkbench() {
      this.workbench = {
        doctorId: undefined,
        doctorName: undefined,
        departmentName: undefined,
        visitDate: this.queryParams.visitDate,
        todayRegistrationCount: 0,
        pendingCount: 0,
        visitedCount: 0,
        cancelCount: 0,
        noShowCount: 0,
        registrationList: [],
        pendingList: [],
        scheduleList: [],
        recentVisitList: []
      }
    },
    goVisit(row) {
      this.$router.push({
        path: "/hospital/visit",
        query: {
          registrationId: row.registrationId,
          doctorId: this.queryParams.doctorId,
          visitDate: this.queryParams.visitDate,
          fromWorkbench: "1"
        }
      })
    },
    formatDoctorLabel(item) {
      return item.doctorName + "（" + item.doctorCode + " / " + item.departmentName + "）"
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
    sourceStatusType(row) {
      if (row.sourceStatus === "2" || Number(row.remainNum || 0) === 0) return "danger"
      if (Number(row.remainNum || 0) <= 5) return "warning"
      return "success"
    }
  }
}
</script>

<style lang="scss" scoped>
.doctor-workbench {
  background: #f5f7fb;
}

.workbench-filter,
.doctor-strip,
.workbench-panel {
  background: #fff;
  border: 1px solid #e5e9f2;
  border-radius: 6px;
}

.workbench-filter {
  padding: 16px 16px 0;
  margin-bottom: 14px;
}

.doctor-strip {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
  margin-bottom: 14px;
}

.doctor-name {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
  line-height: 28px;
}

.doctor-meta {
  display: flex;
  gap: 14px;
  color: #7a8799;
  font-size: 13px;
}

.metric-row {
  margin-bottom: 14px;
}

.metric-card {
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 86px;
  padding: 16px;
  margin-bottom: 14px;
  background: #fff;
  border: 1px solid #e5e9f2;
  border-radius: 6px;
}

.metric-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  border-radius: 50%;
  font-size: 22px;
}

.metric-label {
  color: #7a8799;
  font-size: 13px;
  line-height: 20px;
}

.metric-value {
  color: #1f2d3d;
  font-size: 26px;
  font-weight: 700;
  line-height: 34px;
}

.metric-card--blue .metric-icon {
  color: #2477f3;
  background: #eaf2ff;
}

.metric-card--amber .metric-icon {
  color: #b7791f;
  background: #fff5df;
}

.metric-card--green .metric-icon {
  color: #17a673;
  background: #e9f8f2;
}

.metric-card--red .metric-icon {
  color: #f56c6c;
  background: #fff0f0;
}

.metric-card--gray .metric-icon {
  color: #7a8799;
  background: #f1f3f6;
}

.workbench-panel {
  margin-bottom: 14px;
  padding: 14px 16px 16px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.panel-title {
  color: #1f2d3d;
  font-size: 15px;
  font-weight: 600;
  line-height: 24px;
}

.panel-subtitle {
  color: #7a8799;
  font-size: 12px;
  line-height: 20px;
}
</style>
