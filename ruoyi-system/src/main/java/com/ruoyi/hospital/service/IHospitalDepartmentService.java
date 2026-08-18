package com.ruoyi.hospital.service;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalDepartment;

/**
 * 科室管理Service接口
 *
 * @author ruoyi
 */
public interface IHospitalDepartmentService
{
    public HospitalDepartment selectHospitalDepartmentByDepartmentId(Long departmentId);

    public List<HospitalDepartment> selectHospitalDepartmentList(HospitalDepartment hospitalDepartment);

    public int insertHospitalDepartment(HospitalDepartment hospitalDepartment);

    public int updateHospitalDepartment(HospitalDepartment hospitalDepartment);

    public int deleteHospitalDepartmentByDepartmentIds(Long[] departmentIds);

    public int deleteHospitalDepartmentByDepartmentId(Long departmentId);

    public boolean checkDepartmentCodeUnique(HospitalDepartment hospitalDepartment);
}
