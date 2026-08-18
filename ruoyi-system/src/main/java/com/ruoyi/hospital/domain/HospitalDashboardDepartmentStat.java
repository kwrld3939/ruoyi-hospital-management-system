package com.ruoyi.hospital.domain;

/**
 * 首页科室业务统计
 *
 * @author ruoyi
 */
public class HospitalDashboardDepartmentStat
{
    private Long departmentId;

    private String departmentName;

    private Long registrationCount;

    private Long visitCount;

    public Long getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId)
    {
        this.departmentId = departmentId;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public Long getRegistrationCount()
    {
        return registrationCount;
    }

    public void setRegistrationCount(Long registrationCount)
    {
        this.registrationCount = registrationCount;
    }

    public Long getVisitCount()
    {
        return visitCount;
    }

    public void setVisitCount(Long visitCount)
    {
        this.visitCount = visitCount;
    }
}
