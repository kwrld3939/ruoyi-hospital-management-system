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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.hospital.domain.HospitalRegistration;
import com.ruoyi.hospital.service.IHospitalRegistrationService;

/**
 * 预约挂号Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/registration")
public class HospitalRegistrationController extends BaseController
{
    private static final String STATUS_BOOKED = "0";

    private static final String STATUS_CANCELLED = "1";

    @Autowired
    private IHospitalRegistrationService hospitalRegistrationService;

    /**
     * 查询预约挂号列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:registration:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalRegistration hospitalRegistration)
    {
        startPage();
        List<HospitalRegistration> list = hospitalRegistrationService.selectHospitalRegistrationList(hospitalRegistration);
        return getDataTable(list);
    }

    /**
     * 导出预约挂号列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:registration:export')")
    @Log(title = "预约挂号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HospitalRegistration hospitalRegistration)
    {
        List<HospitalRegistration> list = hospitalRegistrationService.selectHospitalRegistrationList(hospitalRegistration);
        ExcelUtil<HospitalRegistration> util = new ExcelUtil<HospitalRegistration>(HospitalRegistration.class);
        util.exportExcel(response, list, "预约挂号数据");
    }

    /**
     * 获取预约挂号详细信息
     */
    @PreAuthorize("@ss.hasPermi('hospital:registration:query')")
    @GetMapping(value = "/{registrationId}")
    public AjaxResult getInfo(@PathVariable Long registrationId)
    {
        return success(hospitalRegistrationService.selectHospitalRegistrationByRegistrationId(registrationId));
    }

    /**
     * 新增预约挂号
     */
    @PreAuthorize("@ss.hasPermi('hospital:registration:add')")
    @Log(title = "预约挂号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalRegistration hospitalRegistration)
    {
        AjaxResult checkResult = checkRegistration(hospitalRegistration, "新增");
        if (StringUtils.isNotNull(checkResult))
        {
            return checkResult;
        }
        hospitalRegistration.setCreateBy(getUsername());
        return toAjax(hospitalRegistrationService.insertHospitalRegistration(hospitalRegistration));
    }

    /**
     * 修改预约挂号备注
     */
    @PreAuthorize("@ss.hasPermi('hospital:registration:edit')")
    @Log(title = "预约挂号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HospitalRegistration hospitalRegistration)
    {
        if (StringUtils.isNull(hospitalRegistration.getRegistrationId()))
        {
            return error("修改预约挂号失败，挂号ID不能为空");
        }
        hospitalRegistration.setUpdateBy(getUsername());
        return toAjax(hospitalRegistrationService.updateHospitalRegistration(hospitalRegistration));
    }

    /**
     * 取消预约挂号
     */
    @PreAuthorize("@ss.hasPermi('hospital:registration:cancel')")
    @Log(title = "预约挂号", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel")
    public AjaxResult cancel(@RequestBody HospitalRegistration hospitalRegistration)
    {
        if (StringUtils.isNull(hospitalRegistration.getRegistrationId()))
        {
            return error("取消预约挂号失败，挂号ID不能为空");
        }
        hospitalRegistration.setUpdateBy(getUsername());
        return toAjax(hospitalRegistrationService.cancelHospitalRegistration(hospitalRegistration));
    }

    /**
     * 删除预约挂号
     */
    @PreAuthorize("@ss.hasPermi('hospital:registration:remove')")
    @Log(title = "预约挂号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{registrationIds}")
    public AjaxResult remove(@PathVariable Long[] registrationIds)
    {
        return toAjax(hospitalRegistrationService.deleteHospitalRegistrationByRegistrationIds(registrationIds));
    }

    private AjaxResult checkRegistration(HospitalRegistration hospitalRegistration, String action)
    {
        String baseInfoError = hospitalRegistrationService.checkRegistrationBaseInfo(hospitalRegistration);
        if (StringUtils.isNotEmpty(baseInfoError))
        {
            return error(action + "预约挂号失败，" + baseInfoError);
        }
        if (!hospitalRegistrationService.checkPatientSourceUnique(hospitalRegistration))
        {
            return error(action + "预约挂号失败，该患者已预约当前号源");
        }
        if (StringUtils.isNotEmpty(hospitalRegistration.getStatus())
            && !STATUS_BOOKED.equals(hospitalRegistration.getStatus())
            && !STATUS_CANCELLED.equals(hospitalRegistration.getStatus()))
        {
            return error(action + "预约挂号失败，状态值不合法");
        }
        return null;
    }
}
