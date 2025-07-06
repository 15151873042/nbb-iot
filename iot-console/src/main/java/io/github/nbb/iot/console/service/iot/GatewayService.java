package io.github.nbb.iot.console.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.GatewayPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotGateway;

import java.util.List;

/**
 * 网关 Service 接口
 *
 * @author 芋道源码
 */
public interface GatewayService extends IService<IotGateway> {


    PageResult<IotGateway> listPage(GatewayPageDTO dto);

    void updateOnline(List<String> codeList);
}