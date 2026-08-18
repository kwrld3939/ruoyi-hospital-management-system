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
import com.ruoyi.hospital.domain.HospitalScheduleSource;
import com.ruoyi.hospital.service.IHospitalScheduleSourceService;

/**
 * 号源管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/source")
public class HospitalScheduleSourceController extends BaseController
{
    private static final String STATUS_FULL = "2";

    @Autowired
    private IHospitalScheduleSourceService hospitalScheduleSourceService;

    /**
     * 查询号源列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:source:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalScheduleSource hospitalScheduleSource)
    {
        startPage();
        List<HospitalScheduleSource> list = hospitalScheduleSourceService.selectHospitalScheduleSourceList(hospitalScheduleSource);
        return getDataTable(list);
    }

    /**
     * 导出号源列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:source:export')")
    @Log(title = "号源管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HospitalScheduleSource hospitalScheduleSource)
    {
        List<HospitalScheduleSource> list = hospitalScheduleSourceService.selectHospitalScheduleSourceList(hospitalScheduleSource);
        ExcelUtil<HospitalScheduleSource> util = new ExcelUtil<HospitalScheduleSource>(HospitalScheduleSource.class);
        util.exportExcel(response, list, "号源数据");
    }

    /**
     * 获取号源详细信息
     */
    @PreAuthorize("@ss.hasPermi('hospital:source:query')")
    @GetMapping(value = "/{sourceId}")
    public AjaxResult getInfo(@PathVariable Long sourceId)
    {
        return success(hospitalScheduleSourceService.selectHospitalScheduleSourceBySourceId(sourceId));
    }

    /**
     * 新增号源
     */
    @PreAuthorize("@ss.hasPermi('hospital:source:add')")
    @Log(title = "号源管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalScheduleSource hospitalScheduleSource)
    {
        AjaxResult checkResult = checkScheduleSource(hospitalScheduleSource, "新增");
        if (StringUtils.isNotNull(checkResult))
        {
            return checkResult;
        }
        hospitalScheduleSource.setCreateBy(getUsername());
        return toAjax(hospitalScheduleSourceService.insertHospitalScheduleSource(hospitalScheduleSource));
    }

    /**
     * 修改号源
     */
    @PreAuthorize("@ss.hasPermi('hospital:source:edit')")
    @Log(title = "号源管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HospitalScheduleSource hospitalScheduleSource)
    {
        if (StringUtils.isNull(hospitalScheduleSource.getSourceId()))
        {
            return error("修改号源失败，号源ID不能为空");
        }
        AjaxResult checkResult = checkScheduleSource(hospitalScheduleSource, "修改");
        if (StringUtils.isNotNull(checkResult))
        {
            return checkResult;
        }
        hospitalScheduleSource.setUpdateBy(getUsername());
        return toAjax(hospitalScheduleSourceService.updateHospitalScheduleSource(hospitalScheduleSource));
    }

    /**
     * 删除号源
     */
    @PreAuthorize("@ss.hasPermi('hospital:source:remove')")
    @Log(title = "号源管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sourceIds}")
    public AjaxResult remove(@PathVariable Long[] sourceIds)
    {
        return toAjax(hospitalScheduleSourceService.deleteHospitalScheduleSourceBySourceIds(sourceIds));
    }

    private AjaxResult checkScheduleSource(HospitalScheduleSource hospitalScheduleSource, String action)
    {
        if (!isValidStatus(hospitalScheduleSource.getStatus()))
        {
            return error(action + "号源失败，状态值不合法");
        }
        String baseInfoError = hospitalScheduleSourceService.checkScheduleSourceBaseInfo(hospitalScheduleSource);
        if (StringUtils.isNotEmpty(baseInfoError))
        {
            return error(action + "号源失败，" + baseInfoError);
        }
        if (!hospitalScheduleSourceService.checkScheduleSourceUnique(hospitalScheduleSource))
        {
            return error(action + "号源失败，该排班已有号源");
        }
        return null;
    }

    private boolean isValidStatus(String status)
    {
        return StringUtils.isEmpty(status) || UserConstants.NORMAL.equals(status)
            || UserConstants.USER_DISABLE.equals(status) || STATUS_FULL.equals(status);
    }
}
