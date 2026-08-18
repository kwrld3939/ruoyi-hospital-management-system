package com.ruoyi.hospital.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.HospitalPatient;
import com.ruoyi.hospital.mapper.HospitalPatientMapper;
import com.ruoyi.hospital.service.IHospitalPatientService;

/**
 * 患者管理Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalPatientServiceImpl implements IHospitalPatientService
{
    @Autowired
    private HospitalPatientMapper hospitalPatientMapper;

    @Override
    public HospitalPatient selectHospitalPatientByPatientId(Long patientId)
    {
        return hospitalPatientMapper.selectHospitalPatientByPatientId(patientId);
    }

    @Override
    public List<HospitalPatient> selectHospitalPatientList(HospitalPatient hospitalPatient)
    {
        return hospitalPatientMapper.selectHospitalPatientList(hospitalPatient);
    }

    @Override
    public int insertHospitalPatient(HospitalPatient hospitalPatient)
    {
        if (StringUtils.isEmpty(hospitalPatient.getStatus()))
        {
            hospitalPatient.setStatus(UserConstants.NORMAL);
        }
        return hospitalPatientMapper.insertHospitalPatient(hospitalPatient);
    }

    @Override
    public int updateHospitalPatient(HospitalPatient hospitalPatient)
    {
        return hospitalPatientMapper.updateHospitalPatient(hospitalPatient);
    }

    @Override
    public int deleteHospitalPatientByPatientIds(Long[] patientIds)
    {
        return hospitalPatientMapper.deleteHospitalPatientByPatientIds(patientIds);
    }

    @Override
    public int deleteHospitalPatientByPatientId(Long patientId)
    {
        return hospitalPatientMapper.deleteHospitalPatientByPatientId(patientId);
    }

    @Override
    public boolean checkPatientCodeUnique(HospitalPatient hospitalPatient)
    {
        Long patientId = StringUtils.isNull(hospitalPatient.getPatientId()) ? -1L : hospitalPatient.getPatientId();
        HospitalPatient info = hospitalPatientMapper.checkPatientCodeUnique(hospitalPatient.getPatientCode());
        if (StringUtils.isNotNull(info) && info.getPatientId().longValue() != patientId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkIdCardUnique(HospitalPatient hospitalPatient)
    {
        if (StringUtils.isEmpty(hospitalPatient.getIdCard()))
        {
            return UserConstants.UNIQUE;
        }
        Long patientId = StringUtils.isNull(hospitalPatient.getPatientId()) ? -1L : hospitalPatient.getPatientId();
        HospitalPatient info = hospitalPatientMapper.checkIdCardUnique(hospitalPatient.getIdCard());
        if (StringUtils.isNotNull(info) && info.getPatientId().longValue() != patientId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

}
