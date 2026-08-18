package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalSchedule;

/**
 * 医生排班Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalScheduleMapper
{
    public HospitalSchedule selectHospitalScheduleByScheduleId(Long scheduleId);

    public List<HospitalSchedule> selectHospitalScheduleList(HospitalSchedule hospitalSchedule);

    public HospitalSchedule checkScheduleUnique(HospitalSchedule hospitalSchedule);

    public int insertHospitalSchedule(HospitalSchedule hospitalSchedule);

    public int updateHospitalSchedule(HospitalSchedule hospitalSchedule);

    public int deleteHospitalScheduleByScheduleId(Long scheduleId);

    public int deleteHospitalScheduleByScheduleIds(Long[] scheduleIds);
}
