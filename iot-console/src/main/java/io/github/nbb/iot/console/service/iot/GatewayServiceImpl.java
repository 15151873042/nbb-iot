package io.github.nbb.iot.console.service.iot;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.GatewayPageDTO;
import io.github.nbb.iot.console.domain.entity.SysPost;
import io.github.nbb.iot.console.domain.entity.iot.IotGateway;
import io.github.nbb.iot.console.framework.mybatisplus.LambdaQueryWrapperX;
import io.github.nbb.iot.console.mapper.iot.IotGatewayMapper;
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
public class GatewayServiceImpl extends ServiceImpl<IotGatewayMapper, IotGateway> implements GatewayService, SmartInitializingSingleton {

    @Autowired
    private IotGatewayMapper gatewayMapper;

    @Autowired
    private NacosServiceManager nacosServiceManager;
    @Override
    public PageResult<IotGateway> listPage(GatewayPageDTO dto) {
        LambdaQueryWrapper<IotGateway> queryWrapper = new LambdaQueryWrapperX<IotGateway>()
                .eqIfPresent(IotGateway::getIsOnline, dto.getIsOnline())
                .likeIfPresent(IotGateway::getCode, dto.getCode())
                .orderByDesc(IotGateway::getCreateTime);
        PageResult<IotGateway> pageResult = gatewayMapper.selectPage(dto, queryWrapper);
        return pageResult;
    }

    @Override
    public void updateOnline(List<String> codeList) {
        // 1、全部设置不在线
        IotGateway offlineObject = new IotGateway();
        offlineObject.setIsOnline(false);
        gatewayMapper.update(offlineObject, new LambdaUpdateWrapper<>());

        if (CollUtil.isEmpty(codeList)) {
            return;
        }

        LambdaUpdateWrapper<IotGateway> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(IotGateway::getCode, codeList);
        IotGateway updateObject = new IotGateway();
        updateObject.setIsOnline(true);
        gatewayMapper.update(updateObject, updateWrapper);

    }

    @SneakyThrows
    @Override
    public void afterSingletonsInstantiated() {
        NamingService namingService = nacosServiceManager.getNamingService();
        namingService.subscribe("iot-gateway", "DEFAULT_GROUP", event -> {
            if (event instanceof NamingEvent) {
                NamingEvent namingEvent = (NamingEvent) event;

                List<String> onLineClientIds = namingEvent.getInstances().stream()
                        .map(instance -> instance.getMetadata().get("client-id")).collect(Collectors.toList());

                AopProxyUtils.getAopProxy(GatewayServiceImpl.this).updateOnline(onLineClientIds);

            }
        });
    }
}