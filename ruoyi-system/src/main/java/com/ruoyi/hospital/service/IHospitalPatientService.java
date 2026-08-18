package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalPatient;

/**
 * 患者管理Service接口
 *
 * @author ruoyi
 */
public interface IHospitalPatientService
{
    public HospitalPatient selectHospitalPatientByPatientId(Long patientId);

    public List<HospitalPatient> selectHospitalPatientList(HospitalPatient hospitalPatient);

    public int insertHospitalPatient(HospitalPatient hospitalPatient);

    public int updateHospitalPatient(HospitalPatient hospitalPatient);

    public int deleteHospitalPatientByPatientIds(Long[] patientIds);

    public int deleteHospitalPatientByPatientId(Long patientId);

    public boolean checkPatientCodeUnique(HospitalPatient hospitalPatient);

    public boolean checkIdCardUnique(HospitalPatient hospitalPatient);
}
