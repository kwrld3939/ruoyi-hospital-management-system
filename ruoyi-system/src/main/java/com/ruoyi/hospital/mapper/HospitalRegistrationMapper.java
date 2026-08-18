package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalRegistration;

/**
 * 预约挂号Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalRegistrationMapper
{
    public HospitalRegistration selectHospitalRegistrationByRegistrationId(Long registrationId);

    public List<HospitalRegistration> selectHospitalRegistrationList(HospitalRegistration hospitalRegistration);

    public HospitalRegistration checkPatientSourceUnique(HospitalRegistration hospitalRegistration);

    public HospitalRegistration checkRegistrationNoUnique(String registrationNo);

    public List<HospitalRegistration> selectHospitalRegistrationListByPatientId(Long patientId);

    public int insertHospitalRegistration(HospitalRegistration hospitalRegistration);

    public int updateHospitalRegistration(HospitalRegistration hospitalRegistration);

    public int deleteHospitalRegistrationByRegistrationId(Long registrationId);

    public int deleteHospitalRegistrationByRegistrationIds(Long[] registrationIds);
}
