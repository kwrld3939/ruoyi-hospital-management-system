package com.ruoyi.hospital.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 科室管理对象 hospital_department
 *
 * @author ruoyi
 */
public class HospitalDepartment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 科室ID */
    private Long departmentId;

    /** 关联若依部门ID */
    @Excel(name = "组织ID")
    private Long deptId;

    /** 组织名称 */
    @Excel(name = "组织名称")
    private String deptName;

    /** 科室编码 */
    @Excel(name = "科室编码")
    private String departmentCode;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String departmentName;

    /** 科室类型 */
    @Excel(name = "科室类型")
    private String departmentType;

    /** 科室负责人 */
    @Excel(name = "科室负责人")
    private String directorName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 科室位置 */
    @Excel(name = "科室位置")
    private String location;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId)
    {
        this.departmentId = departmentId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    @NotBlank(message = "科室编码不能为空")
    @Size(min = 0, max = 50, message = "科室编码长度不能超过50个字符")
    public String getDepartmentCode()
    {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode)
    {
        this.departmentCode = departmentCode;
    }

    @NotBlank(message = "科室名称不能为空")
    @Size(min = 0, max = 100, message = "科室名称长度不能超过100个字符")
    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    @Size(min = 0, max = 50, message = "科室类型长度不能超过50个字符")
    public String getDepartmentType()
    {
        return departmentType;
    }

    public void setDepartmentType(String departmentType)
    {
        this.departmentType = departmentType;
    }

    @Size(min = 0, max = 50, message = "科室负责人长度不能超过50个字符")
    public String getDirectorName()
    {
        return directorName;
    }

    public void setDirectorName(String directorName)
    {
        this.directorName = directorName;
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

    @Size(min = 0, max = 200, message = "科室位置长度不能超过200个字符")
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
            .append("departmentId", getDepartmentId())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("departmentCode", getDepartmentCode())
            .append("departmentName", getDepartmentName())
            .append("departmentType", getDepartmentType())
            .append("directorName", getDirectorName())
            .append("phone", getPhone())
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
