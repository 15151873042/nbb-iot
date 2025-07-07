package io.github.nbb.iot.console.service.iot;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.GatewayPageDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotGateway;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.framework.mybatisplus.LambdaQueryWrapperX;
import io.github.nbb.iot.console.mapper.iot.IotGatewayMapper;
import io.github.nbb.iot.console.mapper.iot.IotSerialMapper;
import io.github.nbb.iot.console.util.AopProxyUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 网关 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class SerialServiceImpl extends ServiceImpl<IotSerialMapper, IotSerial> implements SerialService {

    @Autowired
    private IotSerialMapper serialMapper;

    @Override
    public PageResult<IotSerial> listPage(SerialPageDTO dto) {
        LambdaQueryWrapper<IotSerial> queryWrapper = new LambdaQueryWrapperX<IotSerial>()
                .eqIfPresent(IotSerial::getIp, dto.getIp())
                .eqIfPresent(IotSerial::getPort, dto.getPort())
                .eqIfPresent(IotSerial::getIsOnline, dto.getIsOnline())
                .orderByDesc(IotSerial::getCreateTime);
        PageResult<IotSerial> pageResult = serialMapper.selectPage(dto, queryWrapper);
        return pageResult;
    }
}