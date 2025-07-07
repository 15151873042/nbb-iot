package io.github.nbb.iot.console.controller.iot;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.GatewayPageDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.SysPost;
import io.github.nbb.iot.console.domain.entity.iot.IotGateway;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.service.iot.GatewayService;
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
    @PostMapping("/add/save")
    public AjaxResult add(@Validated @RequestBody IotSerial serial) {
        boolean result = serialService.save(serial);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     */
    @PostMapping("/add/save")
    public AjaxResult add(@Validated @RequestBody IotSerial serial) {
        boolean result = serialService.save(serial);
        return AjaxResult.success(result);
    }


}