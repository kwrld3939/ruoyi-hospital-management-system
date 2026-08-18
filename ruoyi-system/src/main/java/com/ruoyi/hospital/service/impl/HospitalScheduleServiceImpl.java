package com.ruoyi.hospital.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.HospitalDepartment;
import com.ruoyi.hospital.domain.HospitalDoctor;
import com.ruoyi.hospital.domain.HospitalSchedule;
import com.ruoyi.hospital.mapper.HospitalDepartmentMapper;
import com.ruoyi.hospital.mapper.HospitalDoctorMapper;
import com.ruoyi.hospital.mapper.HospitalScheduleMapper;
import com.ruoyi.hospital.mapper.HospitalScheduleSourceMapper;
import com.ruoyi.hospital.service.IHospitalScheduleService;

/**
 * 医生排班Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalScheduleServiceImpl implements IHospitalScheduleService
{
    @Autowired
    private HospitalScheduleMapper hospitalScheduleMapper;

    @Autowired
    private HospitalDepartmentMapper hospitalDepartmentMapper;

    @Autowired
    private HospitalDoctorMapper hospitalDoctorMapper;

    @Autowired
    private HospitalScheduleSourceMapper hospitalScheduleSourceMapper;

    @Override
    public HospitalSchedule selectHospitalScheduleByScheduleId(Long scheduleId)
    {
        return hospitalScheduleMapper.selectHospitalScheduleByScheduleId(scheduleId);
    }

    @Override
    public List<HospitalSchedule> selectHospitalScheduleList(HospitalSchedule hospitalSchedule)
    {
        return hospitalScheduleMapper.selectHospitalScheduleList(hospitalSchedule);
    }

    @Override
    public int insertHospitalSchedule(HospitalSchedule hospitalSchedule)
    {
        if (StringUtils.isEmpty(hospitalSchedule.getStatus()))
        {
            hospitalSchedule.setStatus(UserConstants.NORMAL);
        }
        return hospitalScheduleMapper.insertHospitalSchedule(hospitalSchedule);
    }

    @Override
    public int updateHospitalSchedule(HospitalSchedule hospitalSchedule)
    {
        return hospitalScheduleMapper.updateHospitalSchedule(hospitalSchedule);
    }

    @Override
    public int deleteHospitalScheduleByScheduleIds(Long[] scheduleIds)
    {
        hospitalScheduleSourceMapper.deleteHospitalScheduleSourceByScheduleIds(scheduleIds);
        return hospitalScheduleMapper.deleteHospitalScheduleByScheduleIds(scheduleIds);
    }

    @Override
    public int deleteHospitalScheduleByScheduleId(Long scheduleId)
    {
        hospitalScheduleSourceMapper.deleteHospitalScheduleSourceByScheduleId(scheduleId);
        return hospitalScheduleMapper.deleteHospitalScheduleByScheduleId(scheduleId);
    }

    @Override
    public boolean checkScheduleUnique(HospitalSchedule hospitalSchedule)
    {
        Long scheduleId = StringUtils.isNull(hospitalSchedule.getScheduleId()) ? -1L : hospitalSchedule.getScheduleId();
        HospitalSchedule info = hospitalScheduleMapper.checkScheduleUnique(hospitalSchedule);
        if (StringUtils.isNotNull(info) && info.getScheduleId().longValue() != scheduleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkScheduleBaseInfo(HospitalSchedule hospitalSchedule)
    {
        HospitalDepartment department = hospitalDepartmentMapper.selectHospitalDepartmentByDepartmentId(hospitalSchedule.getDepartmentId());
        if (StringUtils.isNull(department))
        {
            return "所属科室不存在";
        }
        if (!UserConstants.NORMAL.equals(department.getStatus()))
        {
            return "所属科室已停用";
        }

        HospitalDoctor doctor = hospitalDoctorMapper.selectHospitalDoctorByDoctorId(hospitalSchedule.getDoctorId());
        if (StringUtils.isNull(doctor))
        {
            return "出诊医生不存在";
        }
        if (!UserConstants.NORMAL.equals(doctor.getStatus()))
        {
            return "出诊医生已停用";
        }
        if (!hospitalSchedule.getDepartmentId().equals(doctor.getDepartmentId()))
        {
            return "出诊医生不属于所选科室";
        }
        return null;
    }
}
