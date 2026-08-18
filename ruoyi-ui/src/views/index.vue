<template>
  <div class="hospital-dashboard" v-loading="loading">
    <el-row :gutter="16" class="metric-row">
      <el-col v-for="item in metricCards" :key="item.key" :xs="12" :sm="12" :md="6">
        <div class="metric-card" :class="'metric-card--' + item.type">
          <div class="metric-icon">
            <i :class="item.icon"></i>
          </div>
          <div class="metric-content">
            <div class="metric-label">{{ item.label }}</div>
            <count-to :start-val="0" :end-val="item.value" :duration="1200" class="metric-value" />
            <div class="metric-sub">{{ item.sub }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="16">
        <div class="dashboard-panel">
          <div class="panel-header">
            <div>
              <div class="panel-title">近 7 天门诊趋势</div>
              <div class="panel-subtitle">预约与实际就诊变化</div>
            </div>
          </div>
          <div ref="trendChart" class="chart chart--large"></div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="8">
        <div class="dashboard-panel">
          <div class="panel-header">
            <div>
              <div class="panel-title">今日科室分布</div>
              <div class="panel-subtitle">按科室统计预约和就诊</div>
            </div>
          </div>
          <div ref="departmentChart" class="chart chart--large"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <div class="dashboard-panel">
          <div class="panel-header">
            <div>
              <div class="panel-title">今日待就诊</div>
              <div class="panel-subtitle">已预约但尚未生成就诊记录</div>
            </div>
            <el-tag size="small" type="warning">{{ dashboard.pendingRegistrationCount || 0 }} 人</el-tag>
          </div>
          <el-table :data="dashboard.pendingRegistrationList" size="small" height="310">
            <el-table-column label="患者" prop="patientName" min-width="90" show-overflow-tooltip />
            <el-table-column label="科室" prop="departmentName" min-width="100" show-overflow-tooltip />
            <el-table-column label="医生" prop="doctorName" min-width="90" show-overflow-tooltip />
            <el-table-column label="时段" width="80">
              <template slot-scope="scope">{{ timeSlotText(scope.row.timeSlot) }}</template>
            </el-table-column>
            <el-table-column label="地点" prop="location" min-width="110" show-overflow-tooltip />
            <el-table-column label="挂号单号" prop="registrationNo" min-width="150" show-overflow-tooltip />
          </el-table>
        </div>
      </el-col>

      <el-col :xs="24" :lg="10">
        <div class="dashboard-panel">
          <div class="panel-header">
            <div>
              <div class="panel-title">号源紧张提醒</div>
              <div class="panel-subtitle">剩余 1-5 个且仍可预约</div>
            </div>
            <el-tag size="small" type="danger">{{ dashboard.lowSourceCount || 0 }} 条</el-tag>
          </div>
          <el-table :data="dashboard.lowSourceList" size="small" height="310">
            <el-table-column label="科室" prop="departmentName" min-width="100" show-overflow-tooltip />
            <el-table-column label="医生" prop="doctorName" min-width="90" show-overflow-tooltip />
            <el-table-column label="日期" prop="scheduleDate" width="100" />
            <el-table-column label="时段" width="70">
              <template slot-scope="scope">{{ timeSlotText(scope.row.timeSlot) }}</template>
            </el-table-column>
            <el-table-column label="剩余" width="82" align="center">
              <template slot-scope="scope">
                <el-tag size="mini" :type="scope.row.remainNum <= 2 ? 'danger' : 'warning'">
                  {{ scope.row.remainNum }}/{{ scope.row.totalNum }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import CountTo from 'vue-count-to'
import { getHospitalDashboard } from '@/api/hospital/dashboard'

export default {
  name: 'Index',
  components: {
    CountTo
  },
  data() {
    return {
      loading: false,
      trendChart: null,
      departmentChart: null,
      dashboard: {
        todayRegistrationCount: 0,
        todayVisitCount: 0,
        todayCancelCount: 0,
        todayRemainSourceCount: 0,
        pendingRegistrationCount: 0,
        lowSourceCount: 0,
        departmentStats: [],
        trendList: [],
        pendingRegistrationList: [],
        lowSourceList: []
      }
    }
  },
  computed: {
    metricCards() {
      return [
        {
          key: 'registration',
          label: '今日预约',
          value: Number(this.dashboard.todayRegistrationCount || 0),
          sub: '按就诊日期统计',
          icon: 'el-icon-tickets',
          type: 'blue'
        },
        {
          key: 'visit',
          label: '今日就诊',
          value: Number(this.dashboard.todayVisitCount || 0),
          sub: '已生成就诊记录',
          icon: 'el-icon-first-aid-kit',
          type: 'green'
        },
        {
          key: 'remain',
          label: '今日剩余号源',
          value: Number(this.dashboard.todayRemainSourceCount || 0),
          sub: '可预约号源余量',
          icon: 'el-icon-data-line',
          type: 'amber'
        },
        {
          key: 'cancel',
          label: '今日取消',
          value: Number(this.dashboard.todayCancelCount || 0),
          sub: '已取消预约记录',
          icon: 'el-icon-circle-close',
          type: 'red'
        }
      ]
    }
  },
  mounted() {
    this.getDashboard()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    if (this.trendChart) {
      this.trendChart.dispose()
      this.trendChart = null
    }
    if (this.departmentChart) {
      this.departmentChart.dispose()
      this.departmentChart = null
    }
  },
  methods: {
    getDashboard() {
      this.loading = true
      getHospitalDashboard().then(response => {
        this.dashboard = Object.assign({}, this.dashboard, response.data || {})
        this.$nextTick(() => {
          this.renderTrendChart()
          this.renderDepartmentChart()
        })
      }).finally(() => {
        this.loading = false
      })
    },
    renderTrendChart() {
      if (!this.trendChart) {
        this.trendChart = echarts.init(this.$refs.trendChart)
      }
      const trendList = this.dashboard.trendList || []
      this.trendChart.setOption({
        color: ['#2477f3', '#17a673'],
        tooltip: { trigger: 'axis' },
        legend: {
          top: 0,
          right: 16,
          data: ['预约', '就诊']
        },
        grid: {
          top: 46,
          left: 24,
          right: 24,
          bottom: 24,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: trendList.map(item => item.statDate)
        },
        yAxis: {
          type: 'value',
          minInterval: 1
        },
        series: [
          {
            name: '预约',
            type: 'line',
            smooth: true,
            symbolSize: 7,
            areaStyle: { opacity: 0.08 },
            data: trendList.map(item => item.registrationCount || 0)
          },
          {
            name: '就诊',
            type: 'line',
            smooth: true,
            symbolSize: 7,
            areaStyle: { opacity: 0.08 },
            data: trendList.map(item => item.visitCount || 0)
          }
        ]
      })
    },
    renderDepartmentChart() {
      if (!this.departmentChart) {
        this.departmentChart = echarts.init(this.$refs.departmentChart)
      }
      const stats = this.dashboard.departmentStats || []
      this.departmentChart.setOption({
        color: ['#2477f3', '#e6a23c'],
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: {
          top: 0,
          right: 8,
          data: ['预约', '就诊']
        },
        grid: {
          top: 46,
          left: 18,
          right: 10,
          bottom: 30,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          axisLabel: { interval: 0, rotate: stats.length > 4 ? 30 : 0 },
          data: stats.map(item => item.departmentName)
        },
        yAxis: {
          type: 'value',
          minInterval: 1
        },
        series: [
          {
            name: '预约',
            type: 'bar',
            barMaxWidth: 18,
            data: stats.map(item => item.registrationCount || 0)
          },
          {
            name: '就诊',
            type: 'bar',
            barMaxWidth: 18,
            data: stats.map(item => item.visitCount || 0)
          }
        ]
      })
    },
    resizeCharts() {
      if (this.trendChart) {
        this.trendChart.resize()
      }
      if (this.departmentChart) {
        this.departmentChart.resize()
      }
    },
    timeSlotText(value) {
      const map = {
        1: '上午',
        2: '下午',
        3: '晚上'
      }
      return map[value] || value || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.hospital-dashboard {
  min-height: calc(100vh - 84px);
  padding: 20px;
  background: #f4f6f8;
}

.metric-row {
  margin-bottom: 16px;
}

.metric-card {
  display: flex;
  align-items: center;
  height: 116px;
  padding: 18px;
  margin-bottom: 16px;
  background: #fff;
  border: 1px solid #e8edf3;
  border-radius: 6px;
  box-shadow: 0 1px 2px rgba(18, 38, 63, 0.04);
}

.metric-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 52px;
  width: 52px;
  height: 52px;
  margin-right: 14px;
  border-radius: 6px;
  font-size: 26px;
}

.metric-content {
  min-width: 0;
}

.metric-label {
  color: #6b778c;
  font-size: 13px;
  line-height: 20px;
}

.metric-value {
  display: block;
  margin-top: 4px;
  color: #1f2937;
  font-size: 28px;
  font-weight: 600;
  line-height: 34px;
}

.metric-sub {
  margin-top: 6px;
  color: #8a96a8;
  font-size: 12px;
  line-height: 18px;
  white-space: nowrap;
}

.metric-card--blue .metric-icon {
  color: #2477f3;
  background: #eaf2ff;
}

.metric-card--green .metric-icon {
  color: #17a673;
  background: #e8f7f1;
}

.metric-card--amber .metric-icon {
  color: #b7791f;
  background: #fff4df;
}

.metric-card--red .metric-icon {
  color: #d9534f;
  background: #fdecec;
}

.dashboard-panel {
  margin-bottom: 16px;
  padding: 16px;
  background: #fff;
  border: 1px solid #e8edf3;
  border-radius: 6px;
  box-shadow: 0 1px 2px rgba(18, 38, 63, 0.04);
}

.panel-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  min-height: 42px;
  margin-bottom: 10px;
}

.panel-title {
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
  line-height: 22px;
}

.panel-subtitle {
  margin-top: 3px;
  color: #8a96a8;
  font-size: 12px;
  line-height: 18px;
}

.chart {
  width: 100%;
}

.chart--large {
  height: 336px;
}

@media (max-width: 768px) {
  .hospital-dashboard {
    padding: 12px;
  }

  .metric-card {
    height: 108px;
    padding: 14px;
  }

  .metric-icon {
    flex-basis: 42px;
    width: 42px;
    height: 42px;
    margin-right: 10px;
    font-size: 22px;
  }

  .metric-value {
    font-size: 24px;
  }

  .metric-sub {
    white-space: normal;
  }

  .chart--large {
    height: 300px;
  }
}
</style>
