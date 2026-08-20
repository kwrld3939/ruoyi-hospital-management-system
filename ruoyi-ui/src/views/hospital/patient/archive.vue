<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="患者关键词" prop="patientKeyword">
        <el-input
          v-model="queryParams.patientKeyword"
          placeholder="姓名/编码/手机号/身份证号"
          clearable
          style="width: 260px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="queryParams.gender" placeholder="患者性别" clearable style="width: 120px">
          <el-option label="男" value="0" />
          <el-option label="女" value="1" />
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
          type="success"
          plain
          icon="el-icon-refresh-left"
          size="mini"
          :disabled="multiple"
          @click="handleRestore"
          v-hasRole="['admin']"
        >恢复</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="patientList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="患者ID" align="center" prop="patientId" width="90" />
      <el-table-column label="患者编码" align="center" prop="patientCode" :show-overflow-tooltip="true" />
      <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true" />
      <el-table-column label="性别" align="center" prop="gender" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.gender === '0'">男</span>
          <span v-else-if="scope.row.gender === '1'">女</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="出生日期" align="center" prop="birthDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.birthDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="phone" :show-overflow-tooltip="true" />
      <el-table-column label="身份证号" align="center" prop="idCard" :show-overflow-tooltip="true" />
      <el-table-column label="紧急联系人" align="center" prop="emergencyContact" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="归档时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-refresh-left"
            @click="handleRestore(scope.row)"
            v-hasRole="['admin']"
          >恢复</el-button>
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
  </div>
</template>

<script>
import { listArchivedPatient, restorePatient } from "@/api/hospital/patient"

export default {
  name: "HospitalPatientArchive",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      patientList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientKeyword: undefined,
        gender: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listArchivedPatient(this.queryParams).then(response => {
        this.patientList = response.rows
        this.total = response.total
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.patientId)
      this.multiple = !selection.length
    },
    handleRestore(row) {
      const patientIds = row.patientId || this.ids
      this.$modal.confirm('是否确认恢复患者编号为"' + patientIds + '"的数据项？恢复后患者会回到停用状态。').then(function() {
        return restorePatient(patientIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("恢复成功")
      }).catch(() => {})
    }
  }
}
</script>
