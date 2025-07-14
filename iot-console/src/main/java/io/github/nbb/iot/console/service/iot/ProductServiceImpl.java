package io.github.nbb.iot.console.service.iot;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.nbb.iot.common.domain.IotProductDO;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.ProductAddSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.ProductEditSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.ProductPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import io.github.nbb.iot.console.domain.vo.iot.ProductPageVO;
import io.github.nbb.iot.console.framework.mybatisplus.LambdaQueryWrapperX;
import io.github.nbb.iot.console.mapper.iot.IotProductMapper;
import io.github.nbb.iot.console.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.nbb.iot.common.constants.NacosConfigConstants.IOT_PRODUCT_DATA_ID;

/**
 * 网关 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class ProductServiceImpl extends BasePublishToNacosService<IotProductMapper, IotProduct> implements ProductService {

    @Autowired
    private IotProductMapper productMapper;

    @Override
    public PageResult<ProductPageVO> listPage(ProductPageDTO dto) {
        LambdaQueryWrapper<IotProduct> queryWrapper = new LambdaQueryWrapperX<IotProduct>()
                .likeIfPresent(IotProduct::getProductName, dto.getProductName())
                .orderByDesc(IotProduct::getCreateTime);
        PageResult<IotProduct> iotProductPageResult = productMapper.selectPage(dto, queryWrapper);

        return BeanUtil.copyPageResult(iotProductPageResult, ProductPageVO.class);
    }

    @Override
    public void addSave(ProductAddSaveDTO dto) {
        IotProduct iotProduct = BeanUtil.copyProperties(dto, IotProduct.class);
        productMapper.insert(iotProduct);

        this.publishToNaocs();
    }

    @Override
    public void editSave(ProductEditSaveDTO dto) {
        IotProduct iotProduct = BeanUtil.copyProperties(dto, IotProduct.class);
        productMapper.updateById(iotProduct);

        this.publishToNaocs();
    }

    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);

        this.publishToNaocs();
    }

    @Override
    protected String getPublishDataText() {
        List<IotProduct> serialList = productMapper.listAll();
        List<IotProductDO> doList = BeanUtil.copyToList(serialList, IotProductDO.class);
        return JSONUtil.toJsonStr(doList);
    }

    @Override
    protected String getPublishDataId() {
        return IOT_PRODUCT_DATA_ID;
    }
}
