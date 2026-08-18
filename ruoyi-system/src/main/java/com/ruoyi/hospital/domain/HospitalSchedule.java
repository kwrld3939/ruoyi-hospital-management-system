package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 医生排班对象 hospital_schedule
 *
 * @author ruoyi
 */
public class HospitalSchedule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 排班ID */
    private Long scheduleId;

    /** 科室ID */
    @Excel(name = "科室ID")
    private Long departmentId;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String departmentName;

    /** 医生ID */
    @Excel(name = "医生ID")
    private Long doctorId;

    /** 医生姓名 */
    @Excel(name = "医生姓名")
    private String doctorName;

    /** 医生编码 */
    @Excel(name = "医生编码")
    private String doctorCode;

    /** 医生职称 */
    @Excel(name = "医生职称")
    private String title;

    /** 排班日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "排班日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date scheduleDate;

    /** 时间段（1上午 2下午 3晚上） */
    @Excel(name = "时间段", readConverterExp = "1=上午,2=下午,3=晚上")
    private String timeSlot;

    /** 出诊地点 */
    @Excel(name = "出诊地点")
    private String location;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getScheduleId()
    {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId)
    {
        this.scheduleId = scheduleId;
    }

    @NotNull(message = "所属科室不能为空")
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

    @NotNull(message = "出诊医生不能为空")
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

    public String getDoctorCode()
    {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode)
    {
        this.doctorCode = doctorCode;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @NotNull(message = "排班日期不能为空")
    public Date getScheduleDate()
    {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate)
    {
        this.scheduleDate = scheduleDate;
    }

    @NotBlank(message = "时间段不能为空")
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("scheduleId", getScheduleId())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("doctorId", getDoctorId())
            .append("doctorName", getDoctorName())
            .append("doctorCode", getDoctorCode())
            .append("title", getTitle())
            .append("scheduleDate", getScheduleDate())
            .append("timeSlot", getTimeSlot())
            .append("location", getLocation())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
