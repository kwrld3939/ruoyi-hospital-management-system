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
import com.ruoyi.hospital.domain.HospitalSchedule;
import com.ruoyi.hospital.service.IHospitalScheduleService;

/**
 * 医生排班Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/schedule")
public class HospitalScheduleController extends BaseController
{
    @Autowired
    private IHospitalScheduleService hospitalScheduleService;

    /**
     * 查询医生排班列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:schedule:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalSchedule hospitalSchedule)
    {
        startPage();
        List<HospitalSchedule> list = hospitalScheduleService.selectHospitalScheduleList(hospitalSchedule);
        return getDataTable(list);
    }

    /**
     * 导出医生排班列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:schedule:export')")
    @Log(title = "医生排班", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HospitalSchedule hospitalSchedule)
    {
        List<HospitalSchedule> list = hospitalScheduleService.selectHospitalScheduleList(hospitalSchedule);
        ExcelUtil<HospitalSchedule> util = new ExcelUtil<HospitalSchedule>(HospitalSchedule.class);
        util.exportExcel(response, list, "医生排班数据");
    }

    /**
     * 获取医生排班详细信息
     */
    @PreAuthorize("@ss.hasPermi('hospital:schedule:query')")
    @GetMapping(value = "/{scheduleId}")
    public AjaxResult getInfo(@PathVariable Long scheduleId)
    {
        return success(hospitalScheduleService.selectHospitalScheduleByScheduleId(scheduleId));
    }

    /**
     * 新增医生排班
     */
    @PreAuthorize("@ss.hasPermi('hospital:schedule:add')")
    @Log(title = "医生排班", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalSchedule hospitalSchedule)
    {
        AjaxResult checkResult = checkSchedule(hospitalSchedule, "新增");
        if (StringUtils.isNotNull(checkResult))
        {
            return checkResult;
        }
        hospitalSchedule.setCreateBy(getUsername());
        return toAjax(hospitalScheduleService.insertHospitalSchedule(hospitalSchedule));
    }

    /**
     * 修改医生排班
     */
    @PreAuthorize("@ss.hasPermi('hospital:schedule:edit')")
    @Log(title = "医生排班", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HospitalSchedule hospitalSchedule)
    {
        if (StringUtils.isNull(hospitalSchedule.getScheduleId()))
        {
            return error("修改医生排班失败，排班ID不能为空");
        }
        AjaxResult checkResult = checkSchedule(hospitalSchedule, "修改");
        if (StringUtils.isNotNull(checkResult))
        {
            return checkResult;
        }
        hospitalSchedule.setUpdateBy(getUsername());
        return toAjax(hospitalScheduleService.updateHospitalSchedule(hospitalSchedule));
    }

    /**
     * 删除医生排班
     */
    @PreAuthorize("@ss.hasPermi('hospital:schedule:remove')")
    @Log(title = "医生排班", businessType = BusinessType.DELETE)
    @DeleteMapping("/{scheduleIds}")
    public AjaxResult remove(@PathVariable Long[] scheduleIds)
    {
        return toAjax(hospitalScheduleService.deleteHospitalScheduleByScheduleIds(scheduleIds));
    }

    private AjaxResult checkSchedule(HospitalSchedule hospitalSchedule, String action)
    {
        if (!isValidStatus(hospitalSchedule.getStatus()))
        {
            return error(action + "医生排班失败，状态值不合法");
        }
        if (!isValidTimeSlot(hospitalSchedule.getTimeSlot()))
        {
            return error(action + "医生排班失败，时间段值不合法");
        }
        String baseInfoError = hospitalScheduleService.checkScheduleBaseInfo(hospitalSchedule);
        if (StringUtils.isNotEmpty(baseInfoError))
        {
            return error(action + "医生排班失败，" + baseInfoError);
        }
        if (!hospitalScheduleService.checkScheduleUnique(hospitalSchedule))
        {
            return error(action + "医生排班失败，该医生在当天该时间段已有排班");
        }
        return null;
    }

    private boolean isValidStatus(String status)
    {
        return StringUtils.isEmpty(status) || UserConstants.NORMAL.equals(status) || UserConstants.USER_DISABLE.equals(status);
    }

    private boolean isValidTimeSlot(String timeSlot)
    {
        return "1".equals(timeSlot) || "2".equals(timeSlot) || "3".equals(timeSlot);
    }
}
