package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalDoctor;

/**
 * 医生管理Service接口
 *
 * @author ruoyi
 */
public interface IHospitalDoctorService
{
    public HospitalDoctor selectHospitalDoctorByDoctorId(Long doctorId);

    public List<HospitalDoctor> selectHospitalDoctorList(HospitalDoctor hospitalDoctor);

    public int insertHospitalDoctor(HospitalDoctor hospitalDoctor);

    public int updateHospitalDoctor(HospitalDoctor hospitalDoctor);

    public int deleteHospitalDoctorByDoctorIds(Long[] doctorIds);

    public int deleteHospitalDoctorByDoctorId(Long doctorId);

    public boolean checkDoctorCodeUnique(HospitalDoctor hospitalDoctor);

    public boolean checkDoctorUserUnique(HospitalDoctor hospitalDoctor);
}
