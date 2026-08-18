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
import com.ruoyi.hospital.domain.HospitalVisitRecord;
import com.ruoyi.hospital.service.IHospitalVisitRecordService;

/**
 * 就诊记录Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/visit")
public class HospitalVisitRecordController extends BaseController
{
    @Autowired
    private IHospitalVisitRecordService hospitalVisitRecordService;

    /**
     * 查询就诊记录列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:visit:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalVisitRecord hospitalVisitRecord)
    {
        startPage();
        List<HospitalVisitRecord> list = hospitalVisitRecordService.selectHospitalVisitRecordList(hospitalVisitRecord);
        return getDataTable(list);
    }

    /**
     * 导出就诊记录列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:visit:export')")
    @Log(title = "就诊记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HospitalVisitRecord hospitalVisitRecord)
    {
        List<HospitalVisitRecord> list = hospitalVisitRecordService.selectHospitalVisitRecordList(hospitalVisitRecord);
        ExcelUtil<HospitalVisitRecord> util = new ExcelUtil<HospitalVisitRecord>(HospitalVisitRecord.class);
        util.exportExcel(response, list, "就诊记录数据");
    }

    /**
     * 获取就诊记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('hospital:visit:query')")
    @GetMapping(value = "/{visitId}")
    public AjaxResult getInfo(@PathVariable Long visitId)
    {
        return success(hospitalVisitRecordService.selectHospitalVisitRecordByVisitId(visitId));
    }

    /**
     * 新增就诊记录
     */
    @PreAuthorize("@ss.hasPermi('hospital:visit:add')")
    @Log(title = "就诊记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalVisitRecord hospitalVisitRecord)
    {
        AjaxResult checkResult = checkVisitRecord(hospitalVisitRecord, "新增");
        if (StringUtils.isNotNull(checkResult))
        {
            return checkResult;
        }
        hospitalVisitRecord.setCreateBy(getUsername());
        return toAjax(hospitalVisitRecordService.insertHospitalVisitRecord(hospitalVisitRecord));
    }

    /**
     * 修改就诊记录
     */
    @PreAuthorize("@ss.hasPermi('hospital:visit:edit')")
    @Log(title = "就诊记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HospitalVisitRecord hospitalVisitRecord)
    {
        if (StringUtils.isNull(hospitalVisitRecord.getVisitId()))
        {
            return error("修改就诊记录失败，就诊记录ID不能为空");
        }
        hospitalVisitRecord.setUpdateBy(getUsername());
        return toAjax(hospitalVisitRecordService.updateHospitalVisitRecord(hospitalVisitRecord));
    }

    /**
     * 删除就诊记录
     */
    @PreAuthorize("@ss.hasPermi('hospital:visit:remove')")
    @Log(title = "就诊记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{visitIds}")
    public AjaxResult remove(@PathVariable Long[] visitIds)
    {
        return toAjax(hospitalVisitRecordService.deleteHospitalVisitRecordByVisitIds(visitIds));
    }

    private AjaxResult checkVisitRecord(HospitalVisitRecord hospitalVisitRecord, String action)
    {
        String baseInfoError = hospitalVisitRecordService.checkVisitRecordBaseInfo(hospitalVisitRecord);
        if (StringUtils.isNotEmpty(baseInfoError))
        {
            return error(action + "就诊记录失败，" + baseInfoError);
        }
        if (!hospitalVisitRecordService.checkRegistrationVisitUnique(hospitalVisitRecord))
        {
            return error(action + "就诊记录失败，该挂号记录已有就诊记录");
        }
        return null;
    }
}
