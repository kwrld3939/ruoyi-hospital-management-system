package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 首页低余号源明细
 *
 * @author ruoyi
 */
public class HospitalDashboardSourceItem
{
    private Long sourceId;

    private String departmentName;

    private String doctorName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    private String timeSlot;

    private String location;

    private Integer totalNum;

    private Integer remainNum;

    private String status;

    public Long getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(Long sourceId)
    {
        this.sourceId = sourceId;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
