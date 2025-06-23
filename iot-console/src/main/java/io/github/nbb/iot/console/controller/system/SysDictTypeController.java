package io.github.nbb.iot.console.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.DictTypePageDTO;
import io.github.nbb.iot.console.domain.entity.SysDictType;
import io.github.nbb.iot.console.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private SysDictTypeService dictTypeService;

    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public AjaxResult list(DictTypePageDTO dto) {
        PageResult<SysDictType> result = dictTypeService.listPage(dto);
        return AjaxResult.success(result);
    }

    /// 暂时注释导出
//    @SaCheckPermission("system:dict:export")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysDictType dictType) {
//        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
//        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
//        util.exportExcel(response, list, "字典类型");
//    }

    /**
     * 查询字典类型详细
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(dictTypeService.selectDictTypeById(id));
    }

    /**
     * 新增字典类型
     */
    @SaCheckPermission("system:dict:add")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @SaCheckPermission("system:dict:edit")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @SaCheckPermission("system:dict:remove")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        dictTypeService.deleteDictTypeByIds(ids);
        return success();
    }

    /**
     * 刷新字典缓存
     */
    @SaCheckPermission("system:dict:remove")
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }
}
