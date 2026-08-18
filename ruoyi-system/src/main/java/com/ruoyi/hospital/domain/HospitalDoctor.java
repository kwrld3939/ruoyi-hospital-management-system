package com.ruoyi.hospital.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 医生管理对象 hospital_doctor
 *
 * @author ruoyi
 */
public class HospitalDoctor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 医生ID */
    private Long doctorId;

    /** 所属科室ID */
    @Excel(name = "科室ID")
    private Long departmentId;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String departmentName;

    /** 组织名称 */
    @Excel(name = "组织名称")
    private String deptName;

    /** 绑定系统用户ID */
    @Excel(name = "绑定用户ID")
    private Long userId;

    /** 绑定系统用户名称 */
    @Excel(name = "绑定用户")
    private String userName;

    /** 医生编码 */
    @Excel(name = "医生编码")
    private String doctorCode;

    /** 医生姓名 */
    @Excel(name = "医生姓名")
    private String doctorName;

    private String doctorKeyword;

    /** 性别 */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String gender;

    /** 职称 */
    @Excel(name = "职称")
    private String title;

    /** 专长 */
    @Excel(name = "专长")
    private String specialty;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getDoctorId()
    {
        return doctorId;
    }

    public void setDoctorId(Long doctorId)
    {
        this.doctorId = doctorId;
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

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @NotBlank(message = "医生编码不能为空")
    @Size(min = 0, max = 50, message = "医生编码长度不能超过50个字符")
    public String getDoctorCode()
    {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode)
    {
        this.doctorCode = doctorCode;
    }

    @NotBlank(message = "医生姓名不能为空")
    @Size(min = 0, max = 50, message = "医生姓名长度不能超过50个字符")
    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getDoctorKeyword()
    {
        return doctorKeyword;
    }

    public void setDoctorKeyword(String doctorKeyword)
    {
        this.doctorKeyword = doctorKeyword;
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

    @Size(min = 0, max = 50, message = "职称长度不能超过50个字符")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Size(min = 0, max = 200, message = "专长长度不能超过200个字符")
    public String getSpecialty()
    {
        return specialty;
    }

    public void setSpecialty(String specialty)
    {
        this.specialty = specialty;
    }

    @Size(min = 0, max = 30, message = "联系电话长度不能超过30个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
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
            .append("doctorId", getDoctorId())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("deptName", getDeptName())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("doctorCode", getDoctorCode())
            .append("doctorName", getDoctorName())
            .append("gender", getGender())
            .append("title", getTitle())
            .append("specialty", getSpecialty())
            .append("phone", getPhone())
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
