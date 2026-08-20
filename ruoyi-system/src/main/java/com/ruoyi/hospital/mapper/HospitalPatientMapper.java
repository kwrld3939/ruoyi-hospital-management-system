package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalPatient;

/**
 * 患者管理Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalPatientMapper
{
    public HospitalPatient selectHospitalPatientByPatientId(Long patientId);

    public List<HospitalPatient> selectHospitalPatientList(HospitalPatient hospitalPatient);

    public List<HospitalPatient> selectArchivedHospitalPatientList(HospitalPatient hospitalPatient);

    public HospitalPatient checkPatientCodeUnique(String patientCode);

    public HospitalPatient checkIdCardUnique(String idCard);

    public int insertHospitalPatient(HospitalPatient hospitalPatient);

    public int updateHospitalPatient(HospitalPatient hospitalPatient);

    public int deleteHospitalPatientByPatientId(Long patientId);

    public int deleteHospitalPatientByPatientIds(Long[] patientIds);

    public int archiveHospitalPatientByPatientId(Long patientId);

    public int restoreHospitalPatientByPatientId(Long patientId);
}
