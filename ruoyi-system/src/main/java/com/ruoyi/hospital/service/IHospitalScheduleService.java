package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalSchedule;

/**
 * 医生排班Service接口
 *
 * @author ruoyi
 */
public interface IHospitalScheduleService
{
    public HospitalSchedule selectHospitalScheduleByScheduleId(Long scheduleId);

    public List<HospitalSchedule> selectHospitalScheduleList(HospitalSchedule hospitalSchedule);

    public int insertHospitalSchedule(HospitalSchedule hospitalSchedule);

    public int updateHospitalSchedule(HospitalSchedule hospitalSchedule);

    public int deleteHospitalScheduleByScheduleIds(Long[] scheduleIds);

    public int deleteHospitalScheduleByScheduleId(Long scheduleId);

    public boolean checkScheduleUnique(HospitalSchedule hospitalSchedule);

    public String checkScheduleBaseInfo(HospitalSchedule hospitalSchedule);
}
