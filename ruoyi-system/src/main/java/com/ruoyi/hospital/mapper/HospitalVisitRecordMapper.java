package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalVisitRecord;

/**
 * 就诊记录Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalVisitRecordMapper
{
    public HospitalVisitRecord selectHospitalVisitRecordByVisitId(Long visitId);

    public List<HospitalVisitRecord> selectHospitalVisitRecordList(HospitalVisitRecord hospitalVisitRecord);

    public HospitalVisitRecord checkRegistrationVisitUnique(HospitalVisitRecord hospitalVisitRecord);

    public List<HospitalVisitRecord> selectHospitalVisitRecordListByPatientId(Long patientId);

    public int insertHospitalVisitRecord(HospitalVisitRecord hospitalVisitRecord);

    public int updateHospitalVisitRecord(HospitalVisitRecord hospitalVisitRecord);

    public int deleteHospitalVisitRecordByVisitId(Long visitId);

    public int deleteHospitalVisitRecordByVisitIds(Long[] visitIds);
}
