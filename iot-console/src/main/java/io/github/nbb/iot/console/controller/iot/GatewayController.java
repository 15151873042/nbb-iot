package io.github.nbb.iot.console.controller.iot;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.PostPageDTO;
import io.github.nbb.iot.console.domain.dto.iot.GatewayPageDTO;
import io.github.nbb.iot.console.domain.entity.SysPost;
import io.github.nbb.iot.console.domain.entity.iot.IotGateway;
import io.github.nbb.iot.console.service.iot.GatewayService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/iot/gateway")
@Validated
public class GatewayController {

    @Resource
    private GatewayService gatewayService;

    /**
     * 获取岗位列表
     */
    @GetMapping("/page")
    public AjaxResult list(GatewayPageDTO dto) {
        PageResult<IotGateway> result = gatewayService.listPage(dto);
        return AjaxResult.success(result);
    }


}