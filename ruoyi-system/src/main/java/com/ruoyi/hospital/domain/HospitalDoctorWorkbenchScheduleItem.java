package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 医生工作台排班号源明细
 *
 * @author ruoyi
 */
public class HospitalDoctorWorkbenchScheduleItem
{
    private Long scheduleId;

    private Long sourceId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    private String timeSlot;

    private String location;

    private Integer totalNum;

    private Integer remainNum;

    private String sourceStatus;

    public Long getScheduleId()
    {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId)
    {
        this.scheduleId = scheduleId;
    }

    public Long getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(Long sourceId)
    {
        this.sourceId = sourceId;
    }

    public Date getScheduleDate()
    {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate)
    {
        this.scheduleDate = scheduleDate;
    }

    public String getTimeSlot()
    {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot)
    {
        this.timeSlot = timeSlot;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Integer getTotalNum()
    {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum)
    {
        this.totalNum = totalNum;
    }

    public Integer getRemainNum()
    {
        return remainNum;
    }

    public void setRemainNum(Integer remainNum)
    {
        this.remainNum = remainNum;
    }

    public String getSourceStatus()
    {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus)
    {
        this.sourceStatus = sourceStatus;
    }
}
