package io.github.nbb.iot.console.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.*;
import io.github.nbb.iot.console.domain.entity.iot.IotDevice;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import io.github.nbb.iot.console.domain.vo.iot.ProductPageVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 网关 Service 接口
 *
 * @author 芋道源码
 */
public interface DeviceService extends IService<IotDevice> {


    PageResult<IotDevice> listPage(DevicePageDTO dto);

    @Transactional
    void addSave(DeviceAddSaveDTO dto);

    @Transactional
    void editSave(DeviceEditSaveDTO dto);

    @Transactional
    void deleteById(Long id);
}
