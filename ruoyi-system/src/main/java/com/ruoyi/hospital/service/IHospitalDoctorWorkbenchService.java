package com.ruoyi.hospital.service;

import com.ruoyi.hospital.domain.HospitalDoctorWorkbench;
import com.ruoyi.hospital.domain.HospitalDoctorWorkbenchQuery;

/**
 * 医生工作台Service接口
 *
 * @author ruoyi
 */
public interface IHospitalDoctorWorkbenchService
{
    public HospitalDoctorWorkbench selectDoctorWorkbench(HospitalDoctorWorkbenchQuery query);
}
