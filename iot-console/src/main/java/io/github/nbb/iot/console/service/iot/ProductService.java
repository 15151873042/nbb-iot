package io.github.nbb.iot.console.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.CommonUpdateStatusDTO;
import io.github.nbb.iot.console.domain.dto.iot.*;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.domain.vo.iot.ProductPageVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 网关 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductService extends IService<IotProduct> {


    PageResult<ProductPageVO> listPage(ProductPageDTO dto);

    @Transactional
    void addSave(ProductAddSaveDTO dto);

    @Transactional
    void editSave(ProductEditSaveDTO dto);

    @Transactional
    void deleteById(Long id);

    Map<Long, String> getNameMap(List<Long> productIds);

    @Transactional
    void updateStatus(CommonUpdateStatusDTO dto);
}
