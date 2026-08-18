package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalScheduleSource;

/**
 * 号源管理Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalScheduleSourceMapper
{
    public HospitalScheduleSource selectHospitalScheduleSourceBySourceId(Long sourceId);

    public List<HospitalScheduleSource> selectHospitalScheduleSourceList(HospitalScheduleSource hospitalScheduleSource);

    public HospitalScheduleSource checkScheduleSourceUnique(HospitalScheduleSource hospitalScheduleSource);

    public int insertHospitalScheduleSource(HospitalScheduleSource hospitalScheduleSource);

    public int updateHospitalScheduleSource(HospitalScheduleSource hospitalScheduleSource);

    public int decreaseRemainNum(Long sourceId);

    public int increaseRemainNum(Long sourceId);

    public int deleteHospitalScheduleSourceBySourceId(Long sourceId);

    public int deleteHospitalScheduleSourceBySourceIds(Long[] sourceIds);

    public int deleteHospitalScheduleSourceByScheduleId(Long scheduleId);

    public int deleteHospitalScheduleSourceByScheduleIds(Long[] scheduleIds);
}
