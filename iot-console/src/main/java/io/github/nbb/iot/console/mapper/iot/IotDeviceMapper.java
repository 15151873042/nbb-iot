package io.github.nbb.iot.console.mapper.iot;

import io.github.nbb.iot.console.domain.entity.iot.IotDevice;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import io.github.nbb.iot.console.framework.mybatisplus.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface IotDeviceMapper extends BaseMapperX<IotDevice> {


}
