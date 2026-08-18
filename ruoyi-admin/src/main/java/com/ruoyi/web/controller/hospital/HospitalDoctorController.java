package com.ruoyi.web.controller.hospital;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.hospital.domain.HospitalDoctor;
import com.ruoyi.hospital.service.IHospitalDoctorService;

/**
 * 医生管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/doctor")
public class HospitalDoctorController extends BaseController
{
    @Autowired
    private IHospitalDoctorService hospitalDoctorService;

    /**
     * 查询医生列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:doctor:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalDoctor hospitalDoctor)
    {
        startPage();
        List<HospitalDoctor> list = hospitalDoctorService.selectHospitalDoctorList(hospitalDoctor);
        return getDataTable(list);
    }

    /**
     * 导出医生列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:doctor:export')")
    @Log(title = "医生管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HospitalDoctor hospitalDoctor)
    {
        List<HospitalDoctor> list = hospitalDoctorService.selectHospitalDoctorList(hospitalDoctor);
        ExcelUtil<HospitalDoctor> util = new ExcelUtil<>(HospitalDoctor.class);
        util.exportExcel(response, list, "医生数据");
    }

    /**
     * 获取医生详细信息
     */
    @PreAuthorize("@ss.hasPermi('hospital:doctor:query')")
    @GetMapping(value = "/{doctorId}")
    public AjaxResult getInfo(@PathVariable Long doctorId)
    {
        return success(hospitalDoctorService.selectHospitalDoctorByDoctorId(doctorId));
    }

    /**
     * 新增医生
     */
    @PreAuthorize("@ss.hasPermi('hospital:doctor:add')")
    @Log(title = "医生管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalDoctor hospitalDoctor)
    {
        if (!isValidStatus(hospitalDoctor.getStatus()))
        {
            return error("新增医生失败，状态值不合法");
        }
        if (!hospitalDoctorService.checkDoctorCodeUnique(hospitalDoctor))
        {
            return error("新增医生'" + hospitalDoctor.getDoctorName() + "'失败，医生编码已存在");
        }
        if (!hospitalDoctorService.checkDoctorUserUnique(hospitalDoctor))
        {
            return error("新增医生'" + hospitalDoctor.getDoctorName() + "'失败，绑定账号已被其他医生使用");
        }
        hospitalDoctor.setCreateBy(getUsername());
        return toAjax(hospitalDoctorService.insertHospitalDoctor(hospitalDoctor));
    }

    /**
     * 修改医生
     */
    @PreAuthorize("@ss.hasPermi('hospital:doctor:edit')")
    @Log(title = "医生管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HospitalDoctor hospitalDoctor)
    {
        if (StringUtils.isNull(hospitalDoctor.getDoctorId()))
        {
            return error("修改医生失败，医生ID不能为空");
        }
        if (!isValidStatus(hospitalDoctor.getStatus()))
        {
            return error("修改医生失败，状态值不合法");
        }
        if (!hospitalDoctorService.checkDoctorCodeUnique(hospitalDoctor))
        {
            return error("修改医生'" + hospitalDoctor.getDoctorName() + "'失败，医生编码已存在");
        }
        if (!hospitalDoctorService.checkDoctorUserUnique(hospitalDoctor))
        {
            return error("修改医生'" + hospitalDoctor.getDoctorName() + "'失败，绑定账号已被其他医生使用");
        }
        hospitalDoctor.setUpdateBy(getUsername());
        return toAjax(hospitalDoctorService.updateHospitalDoctor(hospitalDoctor));
    }

    /**
     * 删除医生
     */
    @PreAuthorize("@ss.hasPermi('hospital:doctor:remove')")
    @Log(title = "医生管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{doctorIds}")
    public AjaxResult remove(@PathVariable Long[] doctorIds)
    {
        return toAjax(hospitalDoctorService.deleteHospitalDoctorByDoctorIds(doctorIds));
    }

    private boolean isValidStatus(String status)
    {
        return StringUtils.isEmpty(status) || UserConstants.NORMAL.equals(status) || UserConstants.USER_DISABLE.equals(status);
    }
}
