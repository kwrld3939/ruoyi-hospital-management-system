package com.ruoyi.web.controller.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hospital.service.IHospitalDashboardService;

/**
 * 医院首页看板Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/dashboard")
public class HospitalDashboardController extends BaseController
{
    @Autowired
    private IHospitalDashboardService hospitalDashboardService;

    /**
     * 查询医院首页看板数据
     */
    @GetMapping
    public AjaxResult dashboard()
    {
        return success(hospitalDashboardService.selectHospitalDashboard());
    }
}
