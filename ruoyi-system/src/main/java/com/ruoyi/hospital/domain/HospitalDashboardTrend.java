package com.ruoyi.hospital.domain;

/**
 * 首页近7天趋势
 *
 * @author ruoyi
 */
public class HospitalDashboardTrend
{
    private String statDate;

    private Long registrationCount;

    private Long visitCount;

    public String getStatDate()
    {
        return statDate;
    }

    public void setStatDate(String statDate)
    {
        this.statDate = statDate;
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
