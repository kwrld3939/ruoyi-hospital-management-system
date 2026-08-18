package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预约挂号对象 hospital_registration
 *
 * @author ruoyi
 */
public class HospitalRegistration extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 挂号ID */
    private Long registrationId;

    /** 挂号单号 */
    @Excel(name = "挂号单号")
    private String registrationNo;

    /** 患者ID */
    @Excel(name = "患者ID")
    private Long patientId;

    /** 患者姓名 */
    @Excel(name = "患者姓名")
    private String patientName;

    /** 患者编码 */
    @Excel(name = "患者编码")
    private String patientCode;

    /** 患者手机号 */
    @Excel(name = "患者手机号")
    private String patientPhone;

    /** 患者关键词 */
    private String patientKeyword;

    /** 号源ID */
    @Excel(name = "号源ID")
    private Long sourceId;

    /** 排班ID */
    @Excel(name = "排班ID")
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

    /** 挂号时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "挂号时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;

    /** 就诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "就诊日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitDate;

    /** 时间段（1上午 2下午 3晚上） */
    @Excel(name = "时间段", readConverterExp = "1=上午,2=下午,3=晚上")
    private String timeSlot;

    /** 出诊地点 */
    @Excel(name = "出诊地点")
    private String location;

    /** 状态（0已预约 1已取消 2已就诊 3爽约） */
    @Excel(name = "状态", readConverterExp = "0=已预约,1=已取消,2=已就诊,3=爽约")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getRegistrationId()
    {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId)
    {
        this.registrationId = registrationId;
    }

    public String getRegistrationNo()
    {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo)
    {
        this.registrationNo = registrationNo;
    }

    @NotNull(message = "患者不能为空")
    public Long getPatientId()
    {
        return patientId;
    }

    public void setPatientId(Long patientId)
    {
        this.patientId = patientId;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getPatientCode()
    {
        return patientCode;
    }

    public void setPatientCode(String patientCode)
    {
        this.patientCode = patientCode;
    }

    public String getPatientPhone()
    {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone)
    {
        this.patientPhone = patientPhone;
    }

    public String getPatientKeyword()
    {
        return patientKeyword;
    }

    public void setPatientKeyword(String patientKeyword)
    {
        this.patientKeyword = patientKeyword;
    }

    @NotNull(message = "号源不能为空")
    public Long getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(Long sourceId)
    {
        this.sourceId = sourceId;
    }

    public Long getScheduleId()
    {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId)
    {
        this.scheduleId = scheduleId;
    }

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

    public Date getRegistrationTime()
    {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime)
    {
        this.registrationTime = registrationTime;
    }

    public Date getVisitDate()
    {
        return visitDate;
    }

    public void setVisitDate(Date visitDate)
    {
        this.visitDate = visitDate;
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
            .append("registrationId", getRegistrationId())
            .append("registrationNo", getRegistrationNo())
            .append("patientId", getPatientId())
            .append("patientName", getPatientName())
            .append("patientCode", getPatientCode())
            .append("patientPhone", getPatientPhone())
            .append("patientKeyword", getPatientKeyword())
            .append("sourceId", getSourceId())
            .append("scheduleId", getScheduleId())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("doctorId", getDoctorId())
            .append("doctorName", getDoctorName())
            .append("doctorCode", getDoctorCode())
            .append("registrationTime", getRegistrationTime())
            .append("visitDate", getVisitDate())
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
