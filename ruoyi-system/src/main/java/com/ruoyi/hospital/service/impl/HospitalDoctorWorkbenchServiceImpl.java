package com.ruoyi.hospital.service.impl;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.HospitalDoctor;
import com.ruoyi.hospital.domain.HospitalDoctorWorkbench;
import com.ruoyi.hospital.domain.HospitalDoctorWorkbenchQuery;
import com.ruoyi.hospital.mapper.HospitalDoctorWorkbenchMapper;
import com.ruoyi.hospital.service.IHospitalDoctorWorkbenchService;

/**
 * 医生工作台Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalDoctorWorkbenchServiceImpl implements IHospitalDoctorWorkbenchService
{
    private static final String STATUS_BOOKED = "0";

    private static final String STATUS_CANCELLED = "1";

    private static final String STATUS_VISITED = "2";

    private static final String STATUS_NO_SHOW = "3";

    @Autowired
    private HospitalDoctorWorkbenchMapper hospitalDoctorWorkbenchMapper;

    @Override
    public HospitalDoctorWorkbench selectDoctorWorkbench(HospitalDoctorWorkbenchQuery query)
    {
        if (StringUtils.isNull(query))
        {
            query = new HospitalDoctorWorkbenchQuery();
        }
        HospitalDoctorWorkbench workbench = buildEmptyWorkbench(query);
        Long doctorId = query.getDoctorId();
        if (!SecurityUtils.isAdmin())
        {
            HospitalDoctor boundDoctor = hospitalDoctorWorkbenchMapper.selectDoctorInfoByUserId(SecurityUtils.getUserId());
            if (StringUtils.isNotNull(boundDoctor))
            {
                doctorId = boundDoctor.getDoctorId();
                query.setDoctorId(doctorId);
            }
        }
        if (StringUtils.isNull(doctorId))
        {
            return workbench;
        }

        Date visitDate = StringUtils.isNull(query.getVisitDate()) ? DateUtils.getNowDate() : query.getVisitDate();
        workbench.setVisitDate(visitDate);

        HospitalDoctor doctor = hospitalDoctorWorkbenchMapper.selectDoctorInfo(doctorId);
        if (StringUtils.isNotNull(doctor))
        {
            workbench.setDoctorId(doctor.getDoctorId());
            workbench.setDoctorName(doctor.getDoctorName());
            workbench.setDepartmentName(doctor.getDepartmentName());
        }

        workbench.setTodayRegistrationCount(defaultLong(hospitalDoctorWorkbenchMapper.selectRegistrationCount(doctorId, visitDate)));
        workbench.setPendingCount(defaultLong(hospitalDoctorWorkbenchMapper.selectStatusCount(doctorId, visitDate, STATUS_BOOKED)));
        workbench.setVisitedCount(defaultLong(hospitalDoctorWorkbenchMapper.selectStatusCount(doctorId, visitDate, STATUS_VISITED)));
        workbench.setCancelCount(defaultLong(hospitalDoctorWorkbenchMapper.selectStatusCount(doctorId, visitDate, STATUS_CANCELLED)));
        workbench.setNoShowCount(defaultLong(hospitalDoctorWorkbenchMapper.selectStatusCount(doctorId, visitDate, STATUS_NO_SHOW)));
        workbench.setRegistrationList(hospitalDoctorWorkbenchMapper.selectRegistrationList(doctorId, visitDate));
        workbench.setPendingList(hospitalDoctorWorkbenchMapper.selectPendingList(doctorId, visitDate));
        workbench.setScheduleList(hospitalDoctorWorkbenchMapper.selectScheduleList(doctorId, visitDate));
        workbench.setRecentVisitList(hospitalDoctorWorkbenchMapper.selectRecentVisitList(doctorId));
        return workbench;
    }

    private HospitalDoctorWorkbench buildEmptyWorkbench(HospitalDoctorWorkbenchQuery query)
    {
        HospitalDoctorWorkbench workbench = new HospitalDoctorWorkbench();
        workbench.setDoctorId(StringUtils.isNull(query) ? null : query.getDoctorId());
        workbench.setVisitDate(StringUtils.isNull(query) || StringUtils.isNull(query.getVisitDate()) ? DateUtils.getNowDate() : query.getVisitDate());
        workbench.setTodayRegistrationCount(0L);
        workbench.setPendingCount(0L);
        workbench.setVisitedCount(0L);
        workbench.setCancelCount(0L);
        workbench.setNoShowCount(0L);
        workbench.setRegistrationList(new ArrayList<>());
        workbench.setPendingList(new ArrayList<>());
        workbench.setScheduleList(new ArrayList<>());
        workbench.setRecentVisitList(new ArrayList<>());
        return workbench;
    }

    private Long defaultLong(Long value)
    {
        return value == null ? 0L : value;
    }
}
