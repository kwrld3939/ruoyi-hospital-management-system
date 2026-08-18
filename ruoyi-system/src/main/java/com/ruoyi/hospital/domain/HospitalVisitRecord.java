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
 * 就诊记录对象 hospital_visit_record
 *
 * @author ruoyi
 */
public class HospitalVisitRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 就诊记录ID */
    private Long visitId;

    /** 挂号ID */
    @Excel(name = "挂号ID")
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

    /** 就诊时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "就诊时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;

    /** 就诊日期查询条件 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitDate;

    /** 主诉 */
    @Excel(name = "主诉")
    private String chiefComplaint;

    /** 初步诊断 */
    @Excel(name = "初步诊断")
    private String diagnosis;

    /** 处理意见 */
    @Excel(name = "处理意见")
    private String treatmentAdvice;

    /** 状态（0已就诊 1作废） */
    @Excel(name = "状态", readConverterExp = "0=已就诊,1=作废")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getVisitId()
    {
        return visitId;
    }

    public void setVisitId(Long visitId)
    {
        this.visitId = visitId;
    }

    @NotNull(message = "挂号记录不能为空")
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

    public Date getVisitTime()
    {
        return visitTime;
    }

    public void setVisitTime(Date visitTime)
    {
        this.visitTime = visitTime;
    }

    public Date getVisitDate()
    {
        return visitDate;
    }

    public void setVisitDate(Date visitDate)
    {
        this.visitDate = visitDate;
    }

    @NotBlank(message = "主诉不能为空")
    public String getChiefComplaint()
    {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint)
    {
        this.chiefComplaint = chiefComplaint;
    }

    @NotBlank(message = "初步诊断不能为空")
    public String getDiagnosis()
    {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis)
    {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentAdvice()
    {
        return treatmentAdvice;
    }

    public void setTreatmentAdvice(String treatmentAdvice)
    {
        this.treatmentAdvice = treatmentAdvice;
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
            .append("visitId", getVisitId())
            .append("registrationId", getRegistrationId())
            .append("registrationNo", getRegistrationNo())
            .append("patientId", getPatientId())
            .append("patientName", getPatientName())
            .append("patientCode", getPatientCode())
            .append("patientPhone", getPatientPhone())
            .append("patientKeyword", getPatientKeyword())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("doctorId", getDoctorId())
            .append("doctorName", getDoctorName())
            .append("doctorCode", getDoctorCode())
            .append("visitTime", getVisitTime())
            .append("visitDate", getVisitDate())
            .append("chiefComplaint", getChiefComplaint())
            .append("diagnosis", getDiagnosis())
            .append("treatmentAdvice", getTreatmentAdvice())
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
