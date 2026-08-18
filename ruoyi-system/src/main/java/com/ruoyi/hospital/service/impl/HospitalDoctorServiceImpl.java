package com.ruoyi.hospital.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.HospitalDoctor;
import com.ruoyi.hospital.mapper.HospitalDoctorMapper;
import com.ruoyi.hospital.service.IHospitalDoctorService;

/**
 * 医生管理Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalDoctorServiceImpl implements IHospitalDoctorService
{
    @Autowired
    private HospitalDoctorMapper hospitalDoctorMapper;

    @Override
    public HospitalDoctor selectHospitalDoctorByDoctorId(Long doctorId)
    {
        return hospitalDoctorMapper.selectHospitalDoctorByDoctorId(doctorId);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<HospitalDoctor> selectHospitalDoctorList(HospitalDoctor hospitalDoctor)
    {
        return hospitalDoctorMapper.selectHospitalDoctorList(hospitalDoctor);
    }

    @Override
    public int insertHospitalDoctor(HospitalDoctor hospitalDoctor)
    {
        if (StringUtils.isEmpty(hospitalDoctor.getStatus()))
        {
            hospitalDoctor.setStatus(UserConstants.NORMAL);
        }
        return hospitalDoctorMapper.insertHospitalDoctor(hospitalDoctor);
    }

    @Override
    public int updateHospitalDoctor(HospitalDoctor hospitalDoctor)
    {
        return hospitalDoctorMapper.updateHospitalDoctor(hospitalDoctor);
    }

    @Override
    public int deleteHospitalDoctorByDoctorIds(Long[] doctorIds)
    {
        return hospitalDoctorMapper.deleteHospitalDoctorByDoctorIds(doctorIds);
    }

    @Override
    public int deleteHospitalDoctorByDoctorId(Long doctorId)
    {
        return hospitalDoctorMapper.deleteHospitalDoctorByDoctorId(doctorId);
    }

    @Override
    public boolean checkDoctorCodeUnique(HospitalDoctor hospitalDoctor)
    {
        Long doctorId = StringUtils.isNull(hospitalDoctor.getDoctorId()) ? -1L : hospitalDoctor.getDoctorId();
        HospitalDoctor info = hospitalDoctorMapper.checkDoctorCodeUnique(hospitalDoctor.getDoctorCode());
        if (StringUtils.isNotNull(info) && info.getDoctorId().longValue() != doctorId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkDoctorUserUnique(HospitalDoctor hospitalDoctor)
    {
        if (StringUtils.isNull(hospitalDoctor.getUserId()))
        {
            return UserConstants.UNIQUE;
        }
        Long doctorId = StringUtils.isNull(hospitalDoctor.getDoctorId()) ? -1L : hospitalDoctor.getDoctorId();
        HospitalDoctor info = hospitalDoctorMapper.selectHospitalDoctorByUserId(hospitalDoctor.getUserId());
        if (StringUtils.isNotNull(info) && info.getDoctorId().longValue() != doctorId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
