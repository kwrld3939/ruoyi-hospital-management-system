package com.ruoyi.hospital.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.hospital.domain.HospitalDoctor;
import com.ruoyi.hospital.domain.HospitalDoctorWorkbenchRegistrationItem;
import com.ruoyi.hospital.domain.HospitalDoctorWorkbenchScheduleItem;
import com.ruoyi.hospital.domain.HospitalDoctorWorkbenchVisitItem;

/**
 * 医生工作台Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalDoctorWorkbenchMapper
{
    public HospitalDoctor selectDoctorInfo(@Param("doctorId") Long doctorId);

    public HospitalDoctor selectDoctorInfoByUserId(@Param("userId") Long userId);

    public Long selectRegistrationCount(@Param("doctorId") Long doctorId, @Param("visitDate") Date visitDate);

    public Long selectStatusCount(@Param("doctorId") Long doctorId, @Param("visitDate") Date visitDate, @Param("status") String status);

    public List<HospitalDoctorWorkbenchRegistrationItem> selectRegistrationList(@Param("doctorId") Long doctorId, @Param("visitDate") Date visitDate);

    public List<HospitalDoctorWorkbenchRegistrationItem> selectPendingList(@Param("doctorId") Long doctorId, @Param("visitDate") Date visitDate);

    public List<HospitalDoctorWorkbenchScheduleItem> selectScheduleList(@Param("doctorId") Long doctorId, @Param("visitDate") Date visitDate);

    public List<HospitalDoctorWorkbenchVisitItem> selectRecentVisitList(@Param("doctorId") Long doctorId);
}
