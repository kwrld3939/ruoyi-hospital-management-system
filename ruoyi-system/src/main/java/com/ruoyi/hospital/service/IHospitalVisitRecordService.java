package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalVisitRecord;

/**
 * 就诊记录Service接口
 *
 * @author ruoyi
 */
public interface IHospitalVisitRecordService
{
    public HospitalVisitRecord selectHospitalVisitRecordByVisitId(Long visitId);

    public List<HospitalVisitRecord> selectHospitalVisitRecordList(HospitalVisitRecord hospitalVisitRecord);

    public int insertHospitalVisitRecord(HospitalVisitRecord hospitalVisitRecord);

    public int updateHospitalVisitRecord(HospitalVisitRecord hospitalVisitRecord);

    public int deleteHospitalVisitRecordByVisitIds(Long[] visitIds);

    public int deleteHospitalVisitRecordByVisitId(Long visitId);

    public boolean checkRegistrationVisitUnique(HospitalVisitRecord hospitalVisitRecord);

    public String checkVisitRecordBaseInfo(HospitalVisitRecord hospitalVisitRecord);
}
