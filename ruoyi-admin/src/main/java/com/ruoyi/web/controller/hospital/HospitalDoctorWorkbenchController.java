package com.ruoyi.web.controller.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hospital.domain.HospitalDoctorWorkbenchQuery;
import com.ruoyi.hospital.service.IHospitalDoctorWorkbenchService;

/**
 * 医生工作台Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/workbench/doctor")
public class HospitalDoctorWorkbenchController extends BaseController
{
    @Autowired
    private IHospitalDoctorWorkbenchService hospitalDoctorWorkbenchService;

    /**
     * 查询医生工作台数据
     */
    @PreAuthorize("@ss.hasPermi('hospital:workbench:doctor')")
    @GetMapping
    public AjaxResult dashboard(HospitalDoctorWorkbenchQuery query)
    {
        return success(hospitalDoctorWorkbenchService.selectDoctorWorkbench(query));
    }
}
