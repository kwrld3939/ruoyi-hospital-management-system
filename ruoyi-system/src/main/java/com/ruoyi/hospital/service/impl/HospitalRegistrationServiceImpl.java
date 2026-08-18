package com.ruoyi.hospital.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.Seq;
import com.ruoyi.hospital.domain.HospitalDoctor;
import com.ruoyi.hospital.domain.HospitalPatient;
import com.ruoyi.hospital.domain.HospitalRegistration;
import com.ruoyi.hospital.domain.HospitalScheduleSource;
import com.ruoyi.hospital.mapper.HospitalDoctorMapper;
import com.ruoyi.hospital.mapper.HospitalPatientMapper;
import com.ruoyi.hospital.mapper.HospitalRegistrationMapper;
import com.ruoyi.hospital.mapper.HospitalScheduleSourceMapper;
import com.ruoyi.hospital.service.IHospitalRegistrationService;

/**
 * 预约挂号Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalRegistrationServiceImpl implements IHospitalRegistrationService
{
    private static final String STATUS_BOOKED = "0";

    private static final String STATUS_CANCELLED = "1";

    private static final String SOURCE_STATUS_BOOKABLE = "0";

    @Autowired
    private HospitalRegistrationMapper hospitalRegistrationMapper;

    @Autowired
    private HospitalPatientMapper hospitalPatientMapper;

    @Autowired
    private HospitalScheduleSourceMapper hospitalScheduleSourceMapper;

    @Autowired
    private HospitalDoctorMapper hospitalDoctorMapper;

    @Override
    public HospitalRegistration selectHospitalRegistrationByRegistrationId(Long registrationId)
    {
        HospitalRegistration registration = hospitalRegistrationMapper.selectHospitalRegistrationByRegistrationId(registrationId);
        checkDoctorDataScope(registration);
        return registration;
    }

    @Override
    public List<HospitalRegistration> selectHospitalRegistrationList(HospitalRegistration hospitalRegistration)
    {
        applyDoctorDataScope(hospitalRegistration);
        return hospitalRegistrationMapper.selectHospitalRegistrationList(hospitalRegistration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertHospitalRegistration(HospitalRegistration hospitalRegistration)
    {
        fillRegistrationInfo(hospitalRegistration);
        hospitalRegistration.setRegistrationNo(generateRegistrationNo());
        hospitalRegistration.setRegistrationTime(new Date());
        hospitalRegistration.setStatus(STATUS_BOOKED);
        int effectRows = hospitalScheduleSourceMapper.decreaseRemainNum(hospitalRegistration.getSourceId());
        if (effectRows <= 0)
        {
            throw new RuntimeException("号源不可预约或已约满");
        }
        return hospitalRegistrationMapper.insertHospitalRegistration(hospitalRegistration);
    }

    @Override
    public int updateHospitalRegistration(HospitalRegistration hospitalRegistration)
    {
        HospitalRegistration oldRegistration = hospitalRegistrationMapper.selectHospitalRegistrationByRegistrationId(hospitalRegistration.getRegistrationId());
        checkDoctorDataScope(oldRegistration);
        return hospitalRegistrationMapper.updateHospitalRegistration(hospitalRegistration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelHospitalRegistration(HospitalRegistration hospitalRegistration)
    {
        HospitalRegistration oldRegistration = hospitalRegistrationMapper.selectHospitalRegistrationByRegistrationId(hospitalRegistration.getRegistrationId());
        if (StringUtils.isNull(oldRegistration))
        {
            throw new RuntimeException("预约挂号记录不存在");
        }
        checkDoctorDataScope(oldRegistration);
        if (STATUS_CANCELLED.equals(oldRegistration.getStatus()))
        {
            throw new RuntimeException("预约挂号记录已取消");
        }
        if (!STATUS_BOOKED.equals(oldRegistration.getStatus()))
        {
            throw new RuntimeException("当前状态不允许取消");
        }
        hospitalScheduleSourceMapper.increaseRemainNum(oldRegistration.getSourceId());
        hospitalRegistration.setStatus(STATUS_CANCELLED);
        return hospitalRegistrationMapper.updateHospitalRegistration(hospitalRegistration);
    }

    @Override
    public int deleteHospitalRegistrationByRegistrationIds(Long[] registrationIds)
    {
        for (Long registrationId : registrationIds)
        {
            HospitalRegistration registration = hospitalRegistrationMapper.selectHospitalRegistrationByRegistrationId(registrationId);
            checkDoctorDataScope(registration);
        }
        return hospitalRegistrationMapper.deleteHospitalRegistrationByRegistrationIds(registrationIds);
    }

    @Override
    public int deleteHospitalRegistrationByRegistrationId(Long registrationId)
    {
        HospitalRegistration registration = hospitalRegistrationMapper.selectHospitalRegistrationByRegistrationId(registrationId);
        checkDoctorDataScope(registration);
        return hospitalRegistrationMapper.deleteHospitalRegistrationByRegistrationId(registrationId);
    }

    @Override
    public boolean checkPatientSourceUnique(HospitalRegistration hospitalRegistration)
    {
        Long registrationId = StringUtils.isNull(hospitalRegistration.getRegistrationId()) ? -1L : hospitalRegistration.getRegistrationId();
        HospitalRegistration info = hospitalRegistrationMapper.checkPatientSourceUnique(hospitalRegistration);
        if (StringUtils.isNotNull(info) && info.getRegistrationId().longValue() != registrationId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkRegistrationBaseInfo(HospitalRegistration hospitalRegistration)
    {
        HospitalPatient patient = hospitalPatientMapper.selectHospitalPatientByPatientId(hospitalRegistration.getPatientId());
        if (StringUtils.isNull(patient))
        {
            return "患者不存在";
        }
        if (!UserConstants.NORMAL.equals(patient.getStatus()))
        {
            return "患者已停用";
        }

        HospitalScheduleSource source = hospitalScheduleSourceMapper.selectHospitalScheduleSourceBySourceId(hospitalRegistration.getSourceId());
        if (StringUtils.isNull(source))
        {
            return "号源不存在";
        }
        if (!SOURCE_STATUS_BOOKABLE.equals(source.getStatus()))
        {
            return "号源不可预约";
        }
        if (source.getRemainNum() == null || source.getRemainNum() <= 0)
        {
            return "号源已约满";
        }
        return null;
    }

    private void fillRegistrationInfo(HospitalRegistration hospitalRegistration)
    {
        HospitalScheduleSource source = hospitalScheduleSourceMapper.selectHospitalScheduleSourceBySourceId(hospitalRegistration.getSourceId());
        if (StringUtils.isNotNull(source))
        {
            hospitalRegistration.setScheduleId(source.getScheduleId());
            hospitalRegistration.setDepartmentId(source.getDepartmentId());
            hospitalRegistration.setDoctorId(source.getDoctorId());
            hospitalRegistration.setVisitDate(source.getScheduleDate());
            hospitalRegistration.setTimeSlot(source.getTimeSlot());
        }
    }

    private void applyDoctorDataScope(HospitalRegistration hospitalRegistration)
    {
        if (StringUtils.isNull(hospitalRegistration) || SecurityUtils.isAdmin())
        {
            return;
        }
        HospitalDoctor boundDoctor = hospitalDoctorMapper.selectHospitalDoctorByUserId(SecurityUtils.getUserId());
        if (StringUtils.isNotNull(boundDoctor))
        {
            hospitalRegistration.setDoctorId(boundDoctor.getDoctorId());
        }
    }

    private void checkDoctorDataScope(HospitalRegistration registration)
    {
        if (StringUtils.isNull(registration) || SecurityUtils.isAdmin())
        {
            return;
        }
        HospitalDoctor boundDoctor = hospitalDoctorMapper.selectHospitalDoctorByUserId(SecurityUtils.getUserId());
        if (StringUtils.isNotNull(boundDoctor) && !boundDoctor.getDoctorId().equals(registration.getDoctorId()))
        {
            throw new ServiceException("无权操作其他医生的预约挂号数据");
        }
    }

    private String generateRegistrationNo()
    {
        String registrationNo;
        do
        {
            registrationNo = "REG" + Seq.getId();
        }
        while (StringUtils.isNotNull(hospitalRegistrationMapper.checkRegistrationNoUnique(registrationNo)));
        return registrationNo;
    }
}
