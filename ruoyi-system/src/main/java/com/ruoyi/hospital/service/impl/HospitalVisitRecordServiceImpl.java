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
import com.ruoyi.hospital.domain.HospitalDoctor;
import com.ruoyi.hospital.domain.HospitalRegistration;
import com.ruoyi.hospital.domain.HospitalVisitRecord;
import com.ruoyi.hospital.mapper.HospitalDoctorMapper;
import com.ruoyi.hospital.mapper.HospitalRegistrationMapper;
import com.ruoyi.hospital.mapper.HospitalVisitRecordMapper;
import com.ruoyi.hospital.service.IHospitalVisitRecordService;

/**
 * 就诊记录Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalVisitRecordServiceImpl implements IHospitalVisitRecordService
{
    private static final String REGISTRATION_STATUS_BOOKED = "0";

    private static final String REGISTRATION_STATUS_VISITED = "2";

    private static final String VISIT_STATUS_DONE = "0";

    @Autowired
    private HospitalVisitRecordMapper hospitalVisitRecordMapper;

    @Autowired
    private HospitalRegistrationMapper hospitalRegistrationMapper;

    @Autowired
    private HospitalDoctorMapper hospitalDoctorMapper;

    @Override
    public HospitalVisitRecord selectHospitalVisitRecordByVisitId(Long visitId)
    {
        HospitalVisitRecord visitRecord = hospitalVisitRecordMapper.selectHospitalVisitRecordByVisitId(visitId);
        checkDoctorDataScope(visitRecord);
        return visitRecord;
    }

    @Override
    public List<HospitalVisitRecord> selectHospitalVisitRecordList(HospitalVisitRecord hospitalVisitRecord)
    {
        applyDoctorDataScope(hospitalVisitRecord);
        return hospitalVisitRecordMapper.selectHospitalVisitRecordList(hospitalVisitRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertHospitalVisitRecord(HospitalVisitRecord hospitalVisitRecord)
    {
        fillRegistrationInfo(hospitalVisitRecord);
        hospitalVisitRecord.setVisitTime(new Date());
        hospitalVisitRecord.setStatus(VISIT_STATUS_DONE);
        int rows = hospitalVisitRecordMapper.insertHospitalVisitRecord(hospitalVisitRecord);

        HospitalRegistration registration = new HospitalRegistration();
        registration.setRegistrationId(hospitalVisitRecord.getRegistrationId());
        registration.setStatus(REGISTRATION_STATUS_VISITED);
        registration.setUpdateBy(hospitalVisitRecord.getCreateBy());
        hospitalRegistrationMapper.updateHospitalRegistration(registration);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHospitalVisitRecord(HospitalVisitRecord hospitalVisitRecord)
    {
        HospitalVisitRecord oldVisitRecord = hospitalVisitRecordMapper.selectHospitalVisitRecordByVisitId(hospitalVisitRecord.getVisitId());
        if (StringUtils.isNull(oldVisitRecord))
        {
            throw new RuntimeException("就诊记录不存在");
        }
        checkDoctorDataScope(oldVisitRecord);
        hospitalVisitRecord.setRegistrationId(oldVisitRecord.getRegistrationId());
        hospitalVisitRecord.setPatientId(oldVisitRecord.getPatientId());
        hospitalVisitRecord.setDepartmentId(oldVisitRecord.getDepartmentId());
        hospitalVisitRecord.setDoctorId(oldVisitRecord.getDoctorId());
        int rows = hospitalVisitRecordMapper.updateHospitalVisitRecord(hospitalVisitRecord);
        if (StringUtils.isNotEmpty(hospitalVisitRecord.getStatus()))
        {
            syncRegistrationStatus(oldVisitRecord.getRegistrationId(), hospitalVisitRecord.getUpdateBy());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteHospitalVisitRecordByVisitIds(Long[] visitIds)
    {
        List<HospitalVisitRecord> oldVisitRecords = new java.util.ArrayList<>();
        for (Long visitId : visitIds)
        {
            HospitalVisitRecord oldVisitRecord = hospitalVisitRecordMapper.selectHospitalVisitRecordByVisitId(visitId);
            if (StringUtils.isNotNull(oldVisitRecord))
            {
                checkDoctorDataScope(oldVisitRecord);
                oldVisitRecords.add(oldVisitRecord);
            }
        }
        int rows = hospitalVisitRecordMapper.deleteHospitalVisitRecordByVisitIds(visitIds);
        for (HospitalVisitRecord oldVisitRecord : oldVisitRecords)
        {
            syncRegistrationStatus(oldVisitRecord.getRegistrationId(), oldVisitRecord.getUpdateBy());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteHospitalVisitRecordByVisitId(Long visitId)
    {
        HospitalVisitRecord oldVisitRecord = hospitalVisitRecordMapper.selectHospitalVisitRecordByVisitId(visitId);
        checkDoctorDataScope(oldVisitRecord);
        int rows = hospitalVisitRecordMapper.deleteHospitalVisitRecordByVisitId(visitId);
        if (StringUtils.isNotNull(oldVisitRecord))
        {
            syncRegistrationStatus(oldVisitRecord.getRegistrationId(), oldVisitRecord.getUpdateBy());
        }
        return rows;
    }

    @Override
    public boolean checkRegistrationVisitUnique(HospitalVisitRecord hospitalVisitRecord)
    {
        Long visitId = StringUtils.isNull(hospitalVisitRecord.getVisitId()) ? -1L : hospitalVisitRecord.getVisitId();
        hospitalVisitRecord.setStatus(VISIT_STATUS_DONE);
        HospitalVisitRecord info = hospitalVisitRecordMapper.checkRegistrationVisitUnique(hospitalVisitRecord);
        if (StringUtils.isNotNull(info) && info.getVisitId().longValue() != visitId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkVisitRecordBaseInfo(HospitalVisitRecord hospitalVisitRecord)
    {
        HospitalRegistration registration = hospitalRegistrationMapper.selectHospitalRegistrationByRegistrationId(hospitalVisitRecord.getRegistrationId());
        if (StringUtils.isNull(registration))
        {
            return "挂号记录不存在";
        }
        if (!REGISTRATION_STATUS_BOOKED.equals(registration.getStatus()))
        {
            return "只有已预约状态的挂号记录可以生成就诊记录";
        }
        checkDoctorDataScope(registration.getDoctorId(), "无权为其他医生的预约生成就诊记录");
        return null;
    }

    private void fillRegistrationInfo(HospitalVisitRecord hospitalVisitRecord)
    {
        HospitalRegistration registration = hospitalRegistrationMapper.selectHospitalRegistrationByRegistrationId(hospitalVisitRecord.getRegistrationId());
        if (StringUtils.isNotNull(registration))
        {
            hospitalVisitRecord.setPatientId(registration.getPatientId());
            hospitalVisitRecord.setDepartmentId(registration.getDepartmentId());
            hospitalVisitRecord.setDoctorId(registration.getDoctorId());
        }
    }

    private void syncRegistrationStatus(Long registrationId, String updateBy)
    {
        HospitalVisitRecord query = new HospitalVisitRecord();
        query.setRegistrationId(registrationId);
        query.setStatus(VISIT_STATUS_DONE);
        HospitalVisitRecord effectiveVisitRecord = hospitalVisitRecordMapper.checkRegistrationVisitUnique(query);

        HospitalRegistration registration = new HospitalRegistration();
        registration.setRegistrationId(registrationId);
        registration.setStatus(StringUtils.isNotNull(effectiveVisitRecord) ? REGISTRATION_STATUS_VISITED : REGISTRATION_STATUS_BOOKED);
        registration.setUpdateBy(updateBy);
        hospitalRegistrationMapper.updateHospitalRegistration(registration);
    }

    private void applyDoctorDataScope(HospitalVisitRecord hospitalVisitRecord)
    {
        if (StringUtils.isNull(hospitalVisitRecord) || SecurityUtils.isAdmin())
        {
            return;
        }
        HospitalDoctor boundDoctor = hospitalDoctorMapper.selectHospitalDoctorByUserId(SecurityUtils.getUserId());
        if (StringUtils.isNotNull(boundDoctor))
        {
            hospitalVisitRecord.setDoctorId(boundDoctor.getDoctorId());
        }
    }

    private void checkDoctorDataScope(HospitalVisitRecord visitRecord)
    {
        if (StringUtils.isNull(visitRecord))
        {
            return;
        }
        checkDoctorDataScope(visitRecord.getDoctorId(), "无权操作其他医生的就诊记录数据");
    }

    private void checkDoctorDataScope(Long doctorId, String message)
    {
        if (SecurityUtils.isAdmin())
        {
            return;
        }
        HospitalDoctor boundDoctor = hospitalDoctorMapper.selectHospitalDoctorByUserId(SecurityUtils.getUserId());
        if (StringUtils.isNotNull(boundDoctor) && !boundDoctor.getDoctorId().equals(doctorId))
        {
            throw new ServiceException(message);
        }
    }
}
