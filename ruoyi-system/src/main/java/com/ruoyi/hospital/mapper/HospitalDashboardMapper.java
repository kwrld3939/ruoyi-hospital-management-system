package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalDashboardDepartmentStat;
import com.ruoyi.hospital.domain.HospitalDashboardRegistrationItem;
import com.ruoyi.hospital.domain.HospitalDashboardSourceItem;
import com.ruoyi.hospital.domain.HospitalDashboardTrend;

/**
 * 医院首页看板Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalDashboardMapper
{
    public Long selectTodayRegistrationCount();

    public Long selectTodayVisitCount();

    public Long selectTodayCancelCount();

    public Long selectTodayRemainSourceCount();

    public Long selectPendingRegistrationCount();

    public Long selectLowSourceCount();

    public List<HospitalDashboardDepartmentStat> selectDepartmentStats();

    public List<HospitalDashboardTrend> selectRegistrationTrend();

    public List<HospitalDashboardTrend> selectVisitTrend();

    public List<HospitalDashboardRegistrationItem> selectPendingRegistrationList();

    public List<HospitalDashboardSourceItem> selectLowSourceList();
}
