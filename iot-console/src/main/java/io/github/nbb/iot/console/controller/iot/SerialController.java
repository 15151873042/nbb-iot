package io.github.nbb.iot.console.controller.iot;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.GatewayPageDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialAddSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialEditSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.SysPost;
import io.github.nbb.iot.console.domain.entity.iot.IotGateway;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.service.iot.GatewayService;
import io.github.nbb.iot.console.service.iot.SerialService;
import org.apache.ibatis.annotations.Delete;
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
        IotSerial iotSerial = BeanUtil.copyProperties(dto, IotSerial.class);
        boolean result = serialService.save(iotSerial);
        return AjaxResult.success(result);
    }

    /**
     * 编辑
     */
    @PutMapping("/edit/save")
    public AjaxResult add(@Validated @RequestBody SerialEditSaveDTO dto) {
        IotSerial iotSerial = BeanUtil.copyProperties(dto, IotSerial.class);
        boolean result = serialService.updateById(iotSerial);
        return AjaxResult.success(result);
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public AjaxResult add(@PathVariable("id") Long id) {
        boolean result = serialService.removeById(id);
        return AjaxResult.success(result);
    }
}