package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalScheduleSource;

/**
 * 号源管理Service接口
 *
 * @author ruoyi
 */
public interface IHospitalScheduleSourceService
{
    public HospitalScheduleSource selectHospitalScheduleSourceBySourceId(Long sourceId);

    public List<HospitalScheduleSource> selectHospitalScheduleSourceList(HospitalScheduleSource hospitalScheduleSource);

    public int insertHospitalScheduleSource(HospitalScheduleSource hospitalScheduleSource);

    public int updateHospitalScheduleSource(HospitalScheduleSource hospitalScheduleSource);

    public int deleteHospitalScheduleSourceBySourceIds(Long[] sourceIds);

    public int deleteHospitalScheduleSourceBySourceId(Long sourceId);

    public boolean checkScheduleSourceUnique(HospitalScheduleSource hospitalScheduleSource);

    public String checkScheduleSourceBaseInfo(HospitalScheduleSource hospitalScheduleSource);
}
