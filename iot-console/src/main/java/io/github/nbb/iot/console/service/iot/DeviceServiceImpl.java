package io.github.nbb.iot.console.service.iot;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.nbb.iot.common.domain.IotDeviceDO;
import io.github.nbb.iot.common.domain.IotProductDO;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.*;
import io.github.nbb.iot.console.domain.entity.iot.IotDevice;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import io.github.nbb.iot.console.domain.vo.iot.ProductPageVO;
import io.github.nbb.iot.console.framework.mybatisplus.LambdaQueryWrapperX;
import io.github.nbb.iot.console.mapper.iot.IotDeviceMapper;
import io.github.nbb.iot.console.mapper.iot.IotProductMapper;
import io.github.nbb.iot.console.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.nbb.iot.common.constants.NacosConfigConstants.IOT_DEVICE_DATA_ID;
import static io.github.nbb.iot.common.constants.NacosConfigConstants.IOT_PRODUCT_DATA_ID;

/**
 * 网关 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class DeviceServiceImpl extends BasePublishToNacosService<IotDeviceMapper, IotDevice> implements DeviceService {

    @Autowired
    private IotDeviceMapper deviceMapper;

    @Override
    public PageResult<IotDevice> listPage(DevicePageDTO dto) {
        LambdaQueryWrapper<IotDevice> queryWrapper = new LambdaQueryWrapperX<IotDevice>()
                .likeIfPresent(IotDevice::getDeviceName, dto.getDeviceName())
                .eqIfPresent(IotDevice::getProductId, dto.getProductId())
                .eqIfPresent(IotDevice::getSerialId, dto.getSerialId())
                .orderByDesc(IotDevice::getCreateTime);
        PageResult<IotDevice> iotProductPageResult = deviceMapper.selectPage(dto, queryWrapper);

        return iotProductPageResult;
    }

    @Override
    public void addSave(DeviceAddSaveDTO dto) {
        IotDevice iotDevice = BeanUtil.copyProperties(dto, IotDevice.class);
        deviceMapper.insert(iotDevice);

        this.publishToNaocs();
    }

    @Override
    public void editSave(DeviceEditSaveDTO dto) {
        IotDevice iotDevice = BeanUtil.copyProperties(dto, IotDevice.class);
        deviceMapper.updateById(iotDevice);

        this.publishToNaocs();
    }

    @Override
    public void deleteById(Long id) {
        deviceMapper.deleteById(id);

        this.publishToNaocs();
    }

    @Override
    protected String getPublishDataText() {
        List<IotDevice> serialList = deviceMapper.listAll();
        List<IotDeviceDO> doList = BeanUtil.copyToList(serialList, IotDeviceDO.class);
        return JSONUtil.toJsonStr(doList);
    }

    @Override
    protected String getPublishDataId() {
        return IOT_DEVICE_DATA_ID;
    }
}
