package com.ruoyi.hospital.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hospital.domain.HospitalDashboard;
import com.ruoyi.hospital.domain.HospitalDashboardTrend;
import com.ruoyi.hospital.mapper.HospitalDashboardMapper;
import com.ruoyi.hospital.service.IHospitalDashboardService;

/**
 * 医院首页看板Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalDashboardServiceImpl implements IHospitalDashboardService
{
    @Autowired
    private HospitalDashboardMapper hospitalDashboardMapper;

    @Override
    public HospitalDashboard selectHospitalDashboard()
    {
        HospitalDashboard dashboard = new HospitalDashboard();
        dashboard.setTodayRegistrationCount(defaultLong(hospitalDashboardMapper.selectTodayRegistrationCount()));
        dashboard.setTodayVisitCount(defaultLong(hospitalDashboardMapper.selectTodayVisitCount()));
        dashboard.setTodayCancelCount(defaultLong(hospitalDashboardMapper.selectTodayCancelCount()));
        dashboard.setTodayRemainSourceCount(defaultLong(hospitalDashboardMapper.selectTodayRemainSourceCount()));
        dashboard.setPendingRegistrationCount(defaultLong(hospitalDashboardMapper.selectPendingRegistrationCount()));
        dashboard.setLowSourceCount(defaultLong(hospitalDashboardMapper.selectLowSourceCount()));
        dashboard.setDepartmentStats(hospitalDashboardMapper.selectDepartmentStats());
        dashboard.setTrendList(buildTrendList());
        dashboard.setPendingRegistrationList(hospitalDashboardMapper.selectPendingRegistrationList());
        dashboard.setLowSourceList(hospitalDashboardMapper.selectLowSourceList());
        return dashboard;
    }

    private List<HospitalDashboardTrend> buildTrendList()
    {
        Map<String, HospitalDashboardTrend> trendMap = initLastSevenDays();
        for (HospitalDashboardTrend item : hospitalDashboardMapper.selectRegistrationTrend())
        {
            HospitalDashboardTrend trend = trendMap.get(item.getStatDate());
            if (trend != null)
            {
                trend.setRegistrationCount(defaultLong(item.getRegistrationCount()));
            }
        }
        for (HospitalDashboardTrend item : hospitalDashboardMapper.selectVisitTrend())
        {
            HospitalDashboardTrend trend = trendMap.get(item.getStatDate());
            if (trend != null)
            {
                trend.setVisitCount(defaultLong(item.getVisitCount()));
            }
        }
        return new ArrayList<>(trendMap.values());
    }

    private Map<String, HospitalDashboardTrend> initLastSevenDays()
    {
        Map<String, HospitalDashboardTrend> trendMap = new LinkedHashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        for (int i = 0; i < 7; i++)
        {
            String statDate = formatter.format(calendar.getTime());
            HospitalDashboardTrend trend = new HospitalDashboardTrend();
            trend.setStatDate(statDate);
            trend.setRegistrationCount(0L);
            trend.setVisitCount(0L);
            trendMap.put(statDate, trend);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return trendMap;
    }

    private Long defaultLong(Long value)
    {
        return value == null ? 0L : value;
    }
}
