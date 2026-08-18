package com.ruoyi.hospital.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.HospitalSchedule;
import com.ruoyi.hospital.domain.HospitalScheduleSource;
import com.ruoyi.hospital.mapper.HospitalScheduleMapper;
import com.ruoyi.hospital.mapper.HospitalScheduleSourceMapper;
import com.ruoyi.hospital.service.IHospitalScheduleSourceService;

/**
 * 号源管理Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalScheduleSourceServiceImpl implements IHospitalScheduleSourceService
{
    @Autowired
    private HospitalScheduleSourceMapper hospitalScheduleSourceMapper;

    @Autowired
    private HospitalScheduleMapper hospitalScheduleMapper;

    @Override
    public HospitalScheduleSource selectHospitalScheduleSourceBySourceId(Long sourceId)
    {
        return hospitalScheduleSourceMapper.selectHospitalScheduleSourceBySourceId(sourceId);
    }

    @Override
    public List<HospitalScheduleSource> selectHospitalScheduleSourceList(HospitalScheduleSource hospitalScheduleSource)
    {
        return hospitalScheduleSourceMapper.selectHospitalScheduleSourceList(hospitalScheduleSource);
    }

    @Override
    public int insertHospitalScheduleSource(HospitalScheduleSource hospitalScheduleSource)
    {
        if (StringUtils.isEmpty(hospitalScheduleSource.getStatus()))
        {
            hospitalScheduleSource.setStatus(UserConstants.NORMAL);
        }
        fillScheduleInfo(hospitalScheduleSource);
        return hospitalScheduleSourceMapper.insertHospitalScheduleSource(hospitalScheduleSource);
    }

    @Override
    public int updateHospitalScheduleSource(HospitalScheduleSource hospitalScheduleSource)
    {
        fillOriginalScheduleIdIfNecessary(hospitalScheduleSource);
        fillScheduleInfo(hospitalScheduleSource);
        return hospitalScheduleSourceMapper.updateHospitalScheduleSource(hospitalScheduleSource);
    }

    @Override
    public int deleteHospitalScheduleSourceBySourceIds(Long[] sourceIds)
    {
        return hospitalScheduleSourceMapper.deleteHospitalScheduleSourceBySourceIds(sourceIds);
    }

    @Override
    public int deleteHospitalScheduleSourceBySourceId(Long sourceId)
    {
        return hospitalScheduleSourceMapper.deleteHospitalScheduleSourceBySourceId(sourceId);
    }

    @Override
    public boolean checkScheduleSourceUnique(HospitalScheduleSource hospitalScheduleSource)
    {
        Long sourceId = StringUtils.isNull(hospitalScheduleSource.getSourceId()) ? -1L : hospitalScheduleSource.getSourceId();
        HospitalScheduleSource info = hospitalScheduleSourceMapper.checkScheduleSourceUnique(hospitalScheduleSource);
        if (StringUtils.isNotNull(info) && info.getSourceId().longValue() != sourceId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkScheduleSourceBaseInfo(HospitalScheduleSource hospitalScheduleSource)
    {
        fillOriginalScheduleIdIfNecessary(hospitalScheduleSource);
        HospitalSchedule schedule = hospitalScheduleMapper.selectHospitalScheduleByScheduleId(hospitalScheduleSource.getScheduleId());
        if (StringUtils.isNull(schedule))
        {
            return "排班不存在";
        }
        if (!UserConstants.NORMAL.equals(schedule.getStatus()))
        {
            return "排班已停用";
        }
        if (hospitalScheduleSource.getRemainNum() > hospitalScheduleSource.getTotalNum())
        {
            return "剩余号数不能大于总号数";
        }
        return null;
    }

    private void fillScheduleInfo(HospitalScheduleSource hospitalScheduleSource)
    {
        HospitalSchedule schedule = hospitalScheduleMapper.selectHospitalScheduleByScheduleId(hospitalScheduleSource.getScheduleId());
        if (StringUtils.isNotNull(schedule))
        {
            hospitalScheduleSource.setDepartmentId(schedule.getDepartmentId());
            hospitalScheduleSource.setDoctorId(schedule.getDoctorId());
        }
    }

    private void fillOriginalScheduleIdIfNecessary(HospitalScheduleSource hospitalScheduleSource)
    {
        if (StringUtils.isNull(hospitalScheduleSource.getScheduleId()) && StringUtils.isNotNull(hospitalScheduleSource.getSourceId()))
        {
            HospitalScheduleSource oldSource = hospitalScheduleSourceMapper.selectHospitalScheduleSourceBySourceId(hospitalScheduleSource.getSourceId());
            if (StringUtils.isNotNull(oldSource))
            {
                hospitalScheduleSource.setScheduleId(oldSource.getScheduleId());
            }
        }
    }
}
