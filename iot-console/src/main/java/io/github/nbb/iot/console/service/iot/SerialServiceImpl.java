package io.github.nbb.iot.console.service.iot;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.nbb.iot.common.domain.IotSerialDO;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.SerialAddSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialEditSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.framework.mybatisplus.LambdaQueryWrapperX;
import io.github.nbb.iot.console.mapper.iot.IotSerialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.nbb.iot.common.constants.NacosConfigConstants.IOT_SERIAL_DATA_ID;

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
        return serialMapper.selectPage(dto, queryWrapper);
    }

    @Override
    public void addSave(SerialAddSaveDTO dto) {
        IotSerial iotSerial = BeanUtil.copyProperties(dto, IotSerial.class);
        serialMapper.insert(iotSerial);

        this.publishToNaocs();
    }

    @Override
    public void editSave(SerialEditSaveDTO dto) {
        IotSerial iotSerial = BeanUtil.copyProperties(dto, IotSerial.class);
        serialMapper.updateById(iotSerial);

        this.publishToNaocs();
    }

    @Override
    public void deleteById(Long id) {
        serialMapper.deleteById(id);

        this.publishToNaocs();
    }

    @Override
    protected String getPublishDataText() {
        List<IotSerial> serialList = serialMapper.listAll();
        List<IotSerialDO> doList = BeanUtil.copyToList(serialList, IotSerialDO.class);
        return JSONUtil.toJsonStr(doList);
    }

    @Override
    protected String getPublishDataId() {
        return IOT_SERIAL_DATA_ID;
    }
}
