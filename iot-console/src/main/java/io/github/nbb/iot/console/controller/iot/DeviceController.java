package io.github.nbb.iot.console.controller.iot;

import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.DeviceAddSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.DeviceEditSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.DevicePageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotDevice;
import io.github.nbb.iot.console.domain.vo.iot.DevicePageVO;
import io.github.nbb.iot.console.service.iot.DeviceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/iot/device")
@Validated
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    /**
     * 获取岗位列表
     */
    @GetMapping("/page")
    public AjaxResult list(DevicePageDTO dto) {
        PageResult<DevicePageVO> result = deviceService.listPage(dto);
        return AjaxResult.success(result);
    }


    /**
     * 新增
     */
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        IotDevice iotDevice = deviceService.getById(id);
        return AjaxResult.success(iotDevice);
    }

    /**
     * 新增
     */
    @PostMapping("/add/save")
    public AjaxResult add(@Validated @RequestBody DeviceAddSaveDTO dto) {
        deviceService.addSave(dto);
        return AjaxResult.success(true);
    }

    /**
     * 编辑
     */
    @PutMapping("/edit/save")
    public AjaxResult add(@Validated @RequestBody DeviceEditSaveDTO dto) {
        deviceService.editSave(dto);
        return AjaxResult.success(true);
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public AjaxResult add(@PathVariable("id") Long id) {
        deviceService.deleteById(id);
        return AjaxResult.success(true);
    }
}
