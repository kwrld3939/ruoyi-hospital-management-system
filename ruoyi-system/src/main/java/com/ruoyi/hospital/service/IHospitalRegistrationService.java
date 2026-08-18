package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalRegistration;

/**
 * 预约挂号Service接口
 *
 * @author ruoyi
 */
public interface IHospitalRegistrationService
{
    public HospitalRegistration selectHospitalRegistrationByRegistrationId(Long registrationId);

    public List<HospitalRegistration> selectHospitalRegistrationList(HospitalRegistration hospitalRegistration);

    public int insertHospitalRegistration(HospitalRegistration hospitalRegistration);

    public int updateHospitalRegistration(HospitalRegistration hospitalRegistration);

    public int cancelHospitalRegistration(HospitalRegistration hospitalRegistration);

    public int deleteHospitalRegistrationByRegistrationIds(Long[] registrationIds);

    public int deleteHospitalRegistrationByRegistrationId(Long registrationId);

    public boolean checkPatientSourceUnique(HospitalRegistration hospitalRegistration);

    public String checkRegistrationBaseInfo(HospitalRegistration hospitalRegistration);
}
