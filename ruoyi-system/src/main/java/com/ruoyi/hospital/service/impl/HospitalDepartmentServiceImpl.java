package com.ruoyi.hospital.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hospital.domain.HospitalDepartment;
import com.ruoyi.hospital.mapper.HospitalDepartmentMapper;
import com.ruoyi.hospital.service.IHospitalDepartmentService;

/**
 * 科室管理Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class HospitalDepartmentServiceImpl implements IHospitalDepartmentService
{
    @Autowired
    private HospitalDepartmentMapper hospitalDepartmentMapper;

    @Override
    public HospitalDepartment selectHospitalDepartmentByDepartmentId(Long departmentId)
    {
        return hospitalDepartmentMapper.selectHospitalDepartmentByDepartmentId(departmentId);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<HospitalDepartment> selectHospitalDepartmentList(HospitalDepartment hospitalDepartment)
    {
        return hospitalDepartmentMapper.selectHospitalDepartmentList(hospitalDepartment);
    }

    @Override
    public int insertHospitalDepartment(HospitalDepartment hospitalDepartment)
    {
        if (StringUtils.isEmpty(hospitalDepartment.getStatus()))
        {
            hospitalDepartment.setStatus(UserConstants.NORMAL);
        }
        return hospitalDepartmentMapper.insertHospitalDepartment(hospitalDepartment);
    }

    @Override
    public int updateHospitalDepartment(HospitalDepartment hospitalDepartment)
    {
        return hospitalDepartmentMapper.updateHospitalDepartment(hospitalDepartment);
    }

    @Override
    public int deleteHospitalDepartmentByDepartmentIds(Long[] departmentIds)
    {
        return hospitalDepartmentMapper.deleteHospitalDepartmentByDepartmentIds(departmentIds);
    }

    @Override
    public int deleteHospitalDepartmentByDepartmentId(Long departmentId)
    {
        return hospitalDepartmentMapper.deleteHospitalDepartmentByDepartmentId(departmentId);
    }

    @Override
    public boolean checkDepartmentCodeUnique(HospitalDepartment hospitalDepartment)
    {
        Long departmentId = StringUtils.isNull(hospitalDepartment.getDepartmentId()) ? -1L : hospitalDepartment.getDepartmentId();
        HospitalDepartment info = hospitalDepartmentMapper.checkDepartmentCodeUnique(hospitalDepartment.getDepartmentCode());
        if (StringUtils.isNotNull(info) && info.getDepartmentId().longValue() != departmentId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
