package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 患者管理对象 hospital_patient
 *
 * @author ruoyi
 */
public class HospitalPatient extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 患者ID */
    private Long patientId;

    /** 患者编码 */
    @Excel(name = "患者编码")
    private String patientCode;

    /** 患者姓名 */
    @Excel(name = "患者姓名")
    private String patientName;

    /** 患者关键词 */
    private String patientKeyword;

    /** 性别 */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 联系地址 */
    @Excel(name = "联系地址")
    private String address;

    /** 紧急联系人 */
    @Excel(name = "紧急联系人")
    private String emergencyContact;

    /** 紧急联系电话 */
    @Excel(name = "紧急联系电话")
    private String emergencyPhone;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getPatientId()
    {
        return patientId;
    }

    public void setPatientId(Long patientId)
    {
        this.patientId = patientId;
    }

    @NotBlank(message = "患者编码不能为空")
    @Size(min = 0, max = 50, message = "患者编码长度不能超过50个字符")
    public String getPatientCode()
    {
        return patientCode;
    }

    public void setPatientCode(String patientCode)
    {
        this.patientCode = patientCode;
    }

    @NotBlank(message = "患者姓名不能为空")
    @Size(min = 0, max = 50, message = "患者姓名长度不能超过50个字符")
    public String getPatientName()
    {
        return patientName;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getPatientKeyword()
    {
        return patientKeyword;
    }

    public void setPatientKeyword(String patientKeyword)
    {
        this.patientKeyword = patientKeyword;
    }

    @NotBlank(message = "性别不能为空")
    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    @Size(min = 0, max = 30, message = "身份证号长度不能超过30个字符")
    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    @NotBlank(message = "联系电话不能为空")
    @Size(min = 0, max = 30, message = "联系电话长度不能超过30个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Size(min = 0, max = 200, message = "联系地址长度不能超过200个字符")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Size(min = 0, max = 50, message = "紧急联系人长度不能超过50个字符")
    public String getEmergencyContact()
    {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact)
    {
        this.emergencyContact = emergencyContact;
    }

    @Size(min = 0, max = 30, message = "紧急联系电话长度不能超过30个字符")
    public String getEmergencyPhone()
    {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone)
    {
        this.emergencyPhone = emergencyPhone;
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
            .append("patientId", getPatientId())
            .append("patientCode", getPatientCode())
            .append("patientName", getPatientName())
            .append("patientKeyword", getPatientKeyword())
            .append("gender", getGender())
            .append("birthDate", getBirthDate())
            .append("idCard", getIdCard())
            .append("phone", getPhone())
            .append("address", getAddress())
            .append("emergencyContact", getEmergencyContact())
            .append("emergencyPhone", getEmergencyPhone())
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
