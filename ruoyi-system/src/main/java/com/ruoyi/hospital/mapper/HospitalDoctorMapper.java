package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalDoctor;

/**
 * 医生管理Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalDoctorMapper
{
    public HospitalDoctor selectHospitalDoctorByDoctorId(Long doctorId);

    public HospitalDoctor selectHospitalDoctorByUserId(Long userId);

    public List<HospitalDoctor> selectHospitalDoctorList(HospitalDoctor hospitalDoctor);

    public HospitalDoctor checkDoctorCodeUnique(String doctorCode);

    public int insertHospitalDoctor(HospitalDoctor hospitalDoctor);

    public int updateHospitalDoctor(HospitalDoctor hospitalDoctor);

    public int deleteHospitalDoctorByDoctorId(Long doctorId);

    public int deleteHospitalDoctorByDoctorIds(Long[] doctorIds);
}
