package com.ruoyi.hospital.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 医生工作台数据
 *
 * @author ruoyi
 */
public class HospitalDoctorWorkbench
{
    /** 医生ID */
    private Long doctorId;

    /** 医生姓名 */
    private String doctorName;

    /** 科室名称 */
    private String departmentName;

    /** 工作台日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitDate;

    /** 今日预约总数 */
    private Long todayRegistrationCount;

    /** 今日待接诊数 */
    private Long pendingCount;

    /** 今日已接诊数 */
    private Long visitedCount;

    /** 今日取消数 */
    private Long cancelCount;

    /** 今日爽约数 */
    private Long noShowCount;

    /** 今日预约列表 */
    private List<HospitalDoctorWorkbenchRegistrationItem> registrationList;

    /** 今日待接诊列表 */
    private List<HospitalDoctorWorkbenchRegistrationItem> pendingList;

    /** 今日排班号源 */
    private List<HospitalDoctorWorkbenchScheduleItem> scheduleList;

    /** 最近就诊记录 */
    private List<HospitalDoctorWorkbenchVisitItem> recentVisitList;

    public Long getDoctorId()
    {
        return doctorId;
    }

    public void setDoctorId(Long doctorId)
    {
        this.doctorId = doctorId;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public Date getVisitDate()
    {
        return visitDate;
    }

    public void setVisitDate(Date visitDate)
    {
        this.visitDate = visitDate;
    }

    public Long getTodayRegistrationCount()
    {
        return todayRegistrationCount;
    }

    public void setTodayRegistrationCount(Long todayRegistrationCount)
    {
        this.todayRegistrationCount = todayRegistrationCount;
    }

    public Long getPendingCount()
    {
        return pendingCount;
    }

    public void setPendingCount(Long pendingCount)
    {
        this.pendingCount = pendingCount;
    }

    public Long getVisitedCount()
    {
        return visitedCount;
    }

    public void setVisitedCount(Long visitedCount)
    {
        this.visitedCount = visitedCount;
    }

    public Long getCancelCount()
    {
        return cancelCount;
    }

    public void setCancelCount(Long cancelCount)
    {
        this.cancelCount = cancelCount;
    }

    public Long getNoShowCount()
    {
        return noShowCount;
    }

    public void setNoShowCount(Long noShowCount)
    {
        this.noShowCount = noShowCount;
    }

    public List<HospitalDoctorWorkbenchRegistrationItem> getRegistrationList()
    {
        return registrationList;
    }

    public void setRegistrationList(List<HospitalDoctorWorkbenchRegistrationItem> registrationList)
    {
        this.registrationList = registrationList;
    }

    public List<HospitalDoctorWorkbenchRegistrationItem> getPendingList()
    {
        return pendingList;
    }

    public void setPendingList(List<HospitalDoctorWorkbenchRegistrationItem> pendingList)
    {
        this.pendingList = pendingList;
    }

    public List<HospitalDoctorWorkbenchScheduleItem> getScheduleList()
    {
        return scheduleList;
    }

    public void setScheduleList(List<HospitalDoctorWorkbenchScheduleItem> scheduleList)
    {
        this.scheduleList = scheduleList;
    }

    public List<HospitalDoctorWorkbenchVisitItem> getRecentVisitList()
    {
        return recentVisitList;
    }

    public void setRecentVisitList(List<HospitalDoctorWorkbenchVisitItem> recentVisitList)
    {
        this.recentVisitList = recentVisitList;
    }
}
