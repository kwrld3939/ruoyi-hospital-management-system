package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.HospitalDepartment;

/**
 * 科室管理Mapper接口
 *
 * @author ruoyi
 */
public interface HospitalDepartmentMapper
{
    public HospitalDepartment selectHospitalDepartmentByDepartmentId(Long departmentId);

    public List<HospitalDepartment> selectHospitalDepartmentList(HospitalDepartment hospitalDepartment);

    public HospitalDepartment checkDepartmentCodeUnique(String departmentCode);

    public int insertHospitalDepartment(HospitalDepartment hospitalDepartment);

    public int updateHospitalDepartment(HospitalDepartment hospitalDepartment);

    public int deleteHospitalDepartmentByDepartmentId(Long departmentId);

    public int deleteHospitalDepartmentByDepartmentIds(Long[] departmentIds);
}
