package io.github.nbb.iot.console.controller.iot;

import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.SerialAddSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialEditSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.service.iot.SerialService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/iot/serial")
@Validated
public class SerialController {

    @Resource
    private SerialService serialService;

    /**
     * 获取岗位列表
     */
    @GetMapping("/page")
    public AjaxResult list(SerialPageDTO dto) {
        PageResult<IotSerial> result = serialService.listPage(dto);
        return AjaxResult.success(result);
    }


    /**
     * 新增
     */
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        IotSerial iotSerial = serialService.getById(id);
        return AjaxResult.success(iotSerial);
    }

    /**
     * 新增
     */
    @PostMapping("/add/save")
    public AjaxResult add(@Validated @RequestBody SerialAddSaveDTO dto) {
        serialService.addSave(dto);
        return AjaxResult.success(true);
    }

    /**
     * 编辑
     */
    @PutMapping("/edit/save")
    public AjaxResult add(@Validated @RequestBody SerialEditSaveDTO dto) {
        serialService.editSave(dto);
        return AjaxResult.success(true);
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public AjaxResult add(@PathVariable("id") Long id) {
        serialService.deleteById(id);
        return AjaxResult.success(true);
    }
}
