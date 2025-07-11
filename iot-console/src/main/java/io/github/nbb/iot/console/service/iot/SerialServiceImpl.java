package io.github.nbb.iot.console.service.iot;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.nbb.iot.common.domain.IotSerialDO;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.framework.mybatisplus.LambdaQueryWrapperX;
import io.github.nbb.iot.console.mapper.iot.IotSerialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网关 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class SerialServiceImpl extends BasePublishToNacosService<IotSerialMapper, IotSerial> implements SerialService {

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

    @Override
    protected String getPublishDataText() {
        List<IotSerial> serialList = serialMapper.listAll();
        List<IotSerialDO> doList = BeanUtil.copyToList(serialList, IotSerialDO.class);
        return JSONUtil.toJsonStr(doList);
    }

    @Override
    protected String getPublishDataId() {
        return "iot-serial.json";
    }
}
