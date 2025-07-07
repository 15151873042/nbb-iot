package io.github.nbb.iot.console.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.GatewayPageDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotGateway;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;

import java.util.List;

/**
 * 网关 Service 接口
 *
 * @author 芋道源码
 */
public interface SerialService extends IService<IotSerial> {


    PageResult<IotSerial> listPage(SerialPageDTO dto);
}