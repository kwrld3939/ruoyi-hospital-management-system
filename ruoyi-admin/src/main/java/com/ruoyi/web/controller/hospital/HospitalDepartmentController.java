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
import com.ruoyi.hospital.domain.HospitalDepartment;
import com.ruoyi.hospital.service.IHospitalDepartmentService;

/**
 * 科室管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/hospital/department")
public class HospitalDepartmentController extends BaseController
{
    @Autowired
    private IHospitalDepartmentService hospitalDepartmentService;

    /**
     * 查询科室列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:department:list')")
    @GetMapping("/list")
    public TableDataInfo list(HospitalDepartment hospitalDepartment)
    {
        startPage();
        List<HospitalDepartment> list = hospitalDepartmentService.selectHospitalDepartmentList(hospitalDepartment);
        return getDataTable(list);
    }

    /**
     * 导出科室列表
     */
    @PreAuthorize("@ss.hasPermi('hospital:department:export')")
    @Log(title = "科室管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HospitalDepartment hospitalDepartment)
    {
        List<HospitalDepartment> list = hospitalDepartmentService.selectHospitalDepartmentList(hospitalDepartment);
        ExcelUtil<HospitalDepartment> util = new ExcelUtil<HospitalDepartment>(HospitalDepartment.class);
        util.exportExcel(response, list, "科室数据");
    }

    /**
     * 获取科室详细信息
     */
    @PreAuthorize("@ss.hasPermi('hospital:department:query')")
    @GetMapping(value = "/{departmentId}")
    public AjaxResult getInfo(@PathVariable Long departmentId)
    {
        return success(hospitalDepartmentService.selectHospitalDepartmentByDepartmentId(departmentId));
    }

    /**
     * 新增科室
     */
    @PreAuthorize("@ss.hasPermi('hospital:department:add')")
    @Log(title = "科室管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalDepartment hospitalDepartment)
    {
        if (!isValidStatus(hospitalDepartment.getStatus()))
        {
            return error("新增科室失败，状态值不合法");
        }
        if (!hospitalDepartmentService.checkDepartmentCodeUnique(hospitalDepartment))
        {
            return error("新增科室'" + hospitalDepartment.getDepartmentName() + "'失败，科室编码已存在");
        }
        hospitalDepartment.setCreateBy(getUsername());
        return toAjax(hospitalDepartmentService.insertHospitalDepartment(hospitalDepartment));
    }

    /**
     * 修改科室
     */
    @PreAuthorize("@ss.hasPermi('hospital:department:edit')")
    @Log(title = "科室管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HospitalDepartment hospitalDepartment)
    {
        if (StringUtils.isNull(hospitalDepartment.getDepartmentId()))
        {
            return error("修改科室失败，科室ID不能为空");
        }
        if (!isValidStatus(hospitalDepartment.getStatus()))
        {
            return error("修改科室失败，状态值不合法");
        }
        if (!hospitalDepartmentService.checkDepartmentCodeUnique(hospitalDepartment))
        {
            return error("修改科室'" + hospitalDepartment.getDepartmentName() + "'失败，科室编码已存在");
        }
        hospitalDepartment.setUpdateBy(getUsername());
        return toAjax(hospitalDepartmentService.updateHospitalDepartment(hospitalDepartment));
    }

    /**
     * 删除科室
     */
    @PreAuthorize("@ss.hasPermi('hospital:department:remove')")
    @Log(title = "科室管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{departmentIds}")
    public AjaxResult remove(@PathVariable Long[] departmentIds)
    {
        return toAjax(hospitalDepartmentService.deleteHospitalDepartmentByDepartmentIds(departmentIds));
    }

    private boolean isValidStatus(String status)
    {
        return StringUtils.isEmpty(status) || UserConstants.NORMAL.equals(status) || UserConstants.DEPT_DISABLE.equals(status);
    }
}
