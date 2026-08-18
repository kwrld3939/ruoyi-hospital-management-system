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
import com.ruoyi.hospital.domain.HospitalPatient;
import com.ruoyi.hospital.service.IHospitalPatientService;

/**
 * 患者管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/patient")
public class HospitalPatientController extends BaseController
{
    @Autowired
    private IHospitalPatientService hospitalPatientService;

    /**
     * 查询患者列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:patient:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalPatient hospitalPatient)
    {
        startPage();
        List<HospitalPatient> list = hospitalPatientService.selectHospitalPatientList(hospitalPatient);
        return getDataTable(list);
    }

    /**
     * 导出患者列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:patient:export')")
    @Log(title = "患者管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HospitalPatient hospitalPatient)
    {
        List<HospitalPatient> list = hospitalPatientService.selectHospitalPatientList(hospitalPatient);
        ExcelUtil<HospitalPatient> util = new ExcelUtil<>(HospitalPatient.class);
        util.exportExcel(response, list, "患者数据");
    }

    /**
     * 获取患者详细信息
     */
    @PreAuthorize("@ss.hasPermi('hospital:patient:query')")
    @GetMapping(value = "/{patientId}")
    public AjaxResult getInfo(@PathVariable Long patientId)
    {
        return success(hospitalPatientService.selectHospitalPatientByPatientId(patientId));
    }

    /**
     * 新增患者
     */
    @PreAuthorize("@ss.hasPermi('hospital:patient:add')")
    @Log(title = "患者管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalPatient hospitalPatient)
    {
        if (!isValidStatus(hospitalPatient.getStatus()))
        {
            return error("新增患者失败，状态值不合法");
        }
        if (!isValidGender(hospitalPatient.getGender()))
        {
            return error("新增患者失败，性别值不合法");
        }
        if (!hospitalPatientService.checkPatientCodeUnique(hospitalPatient))
        {
            return error("新增患者'" + hospitalPatient.getPatientName() + "'失败，患者编码已存在");
        }
        if (!hospitalPatientService.checkIdCardUnique(hospitalPatient))
        {
            return error("新增患者'" + hospitalPatient.getPatientName() + "'失败，身份证号已存在");
        }
        hospitalPatient.setCreateBy(getUsername());
        return toAjax(hospitalPatientService.insertHospitalPatient(hospitalPatient));
    }

    /**
     * 修改患者
     */
    @PreAuthorize("@ss.hasPermi('hospital:patient:edit')")
    @Log(title = "患者管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HospitalPatient hospitalPatient)
    {
        if (StringUtils.isNull(hospitalPatient.getPatientId()))
        {
            return error("修改患者失败，患者ID不能为空");
        }
        if (!isValidStatus(hospitalPatient.getStatus()))
        {
            return error("修改患者失败，状态值不合法");
        }
        if (!isValidGender(hospitalPatient.getGender()))
        {
            return error("修改患者失败，性别值不合法");
        }
        if (!hospitalPatientService.checkPatientCodeUnique(hospitalPatient))
        {
            return error("修改患者'" + hospitalPatient.getPatientName() + "'失败，患者编码已存在");
        }
        if (!hospitalPatientService.checkIdCardUnique(hospitalPatient))
        {
            return error("修改患者'" + hospitalPatient.getPatientName() + "'失败，身份证号已存在");
        }
        hospitalPatient.setUpdateBy(getUsername());
        return toAjax(hospitalPatientService.updateHospitalPatient(hospitalPatient));
    }

    /**
     * 删除患者
     */
    @PreAuthorize("@ss.hasPermi('hospital:patient:remove')")
    @Log(title = "患者管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{patientIds}")
    public AjaxResult remove(@PathVariable Long[] patientIds)
    {
        return toAjax(hospitalPatientService.deleteHospitalPatientByPatientIds(patientIds));
    }

    private boolean isValidStatus(String status)
    {
        return StringUtils.isEmpty(status) || UserConstants.NORMAL.equals(status) || UserConstants.USER_DISABLE.equals(status);
    }

    private boolean isValidGender(String gender)
    {
        return "0".equals(gender) || "1".equals(gender);
    }
}
