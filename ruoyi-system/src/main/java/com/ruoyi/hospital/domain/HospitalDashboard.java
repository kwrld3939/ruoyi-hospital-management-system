package com.ruoyi.hospital.domain;

import java.util.List;

/**
 * 医院首页看板数据
 *
 * @author ruoyi
 */
public class HospitalDashboard
{
    /** 今日预约数 */
    private Long todayRegistrationCount;

    /** 今日就诊数 */
    private Long todayVisitCount;

    /** 今日取消预约数 */
    private Long todayCancelCount;

    /** 今日剩余号源数 */
    private Long todayRemainSourceCount;

    /** 今日待就诊数 */
    private Long pendingRegistrationCount;

    /** 低余号源数 */
    private Long lowSourceCount;

    /** 科室业务统计 */
    private List<HospitalDashboardDepartmentStat> departmentStats;

    /** 近7天趋势 */
    private List<HospitalDashboardTrend> trendList;

    /** 今日待就诊列表 */
    private List<HospitalDashboardRegistrationItem> pendingRegistrationList;

    /** 低余号源列表 */
    private List<HospitalDashboardSourceItem> lowSourceList;

    public Long getTodayRegistrationCount()
    {
        return todayRegistrationCount;
    }

    public void setTodayRegistrationCount(Long todayRegistrationCount)
    {
        this.todayRegistrationCount = todayRegistrationCount;
    }

    public Long getTodayVisitCount()
    {
        return todayVisitCount;
    }

    public void setTodayVisitCount(Long todayVisitCount)
    {
        this.todayVisitCount = todayVisitCount;
    }

    public Long getTodayCancelCount()
    {
        return todayCancelCount;
    }

    public void setTodayCancelCount(Long todayCancelCount)
    {
        this.todayCancelCount = todayCancelCount;
    }

    public Long getTodayRemainSourceCount()
    {
        return todayRemainSourceCount;
    }

    public void setTodayRemainSourceCount(Long todayRemainSourceCount)
    {
        this.todayRemainSourceCount = todayRemainSourceCount;
    }

    public Long getPendingRegistrationCount()
    {
        return pendingRegistrationCount;
    }

    public void setPendingRegistrationCount(Long pendingRegistrationCount)
    {
        this.pendingRegistrationCount = pendingRegistrationCount;
    }

    public Long getLowSourceCount()
    {
        return lowSourceCount;
    }

    public void setLowSourceCount(Long lowSourceCount)
    {
        this.lowSourceCount = lowSourceCount;
    }

    public List<HospitalDashboardDepartmentStat> getDepartmentStats()
    {
        return departmentStats;
    }

    public void setDepartmentStats(List<HospitalDashboardDepartmentStat> departmentStats)
    {
        this.departmentStats = departmentStats;
    }

    public List<HospitalDashboardTrend> getTrendList()
    {
        return trendList;
    }

    public void setTrendList(List<HospitalDashboardTrend> trendList)
    {
        this.trendList = trendList;
    }

    public List<HospitalDashboardRegistrationItem> getPendingRegistrationList()
    {
        return pendingRegistrationList;
    }

    public void setPendingRegistrationList(List<HospitalDashboardRegistrationItem> pendingRegistrationList)
    {
        this.pendingRegistrationList = pendingRegistrationList;
    }

    public List<HospitalDashboardSourceItem> getLowSourceList()
    {
        return lowSourceList;
    }

    public void setLowSourceList(List<HospitalDashboardSourceItem> lowSourceList)
    {
        this.lowSourceList = lowSourceList;
    }
}
