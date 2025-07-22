package io.github.nbb.iot.console.controller.iot;

import io.github.nbb.iot.console.domain.AjaxResult;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.CommonUpdateStatusDTO;
import io.github.nbb.iot.console.domain.dto.iot.*;
import io.github.nbb.iot.console.domain.entity.iot.IotProduct;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.domain.vo.iot.ProductPageVO;
import io.github.nbb.iot.console.domain.vo.iot.SerialNameVO;
import io.github.nbb.iot.console.service.iot.ProductService;
import io.github.nbb.iot.console.service.iot.SerialService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/iot/product")
@Validated
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 列表
     */
    @GetMapping("/page")
    public AjaxResult list(ProductPageDTO dto) {
        PageResult<ProductPageVO> result = productService.listPage(dto);
        return AjaxResult.success(result);
    }

    @GetMapping("/list-all-name")
    public AjaxResult listAllName() {
        List<IotProduct> result = productService.list();
        return AjaxResult.success(result);
    }


    /**
     * 详情
     */
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        IotProduct iotProduct = productService.getById(id);
        return AjaxResult.success(iotProduct);
    }

    /**
     * 新增
     */
    @PostMapping("/add/save")
    public AjaxResult add(@Validated @RequestBody ProductAddSaveDTO dto) {
        productService.addSave(dto);
        return AjaxResult.success(true);
    }

    /**
     * 编辑
     */
    @PutMapping("/edit/save")
    public AjaxResult add(@Validated @RequestBody ProductEditSaveDTO dto) {
        productService.editSave(dto);
        return AjaxResult.success(true);
    }

    @PutMapping("/update-status")
    public AjaxResult add(@Validated @RequestBody CommonUpdateStatusDTO dto) {
        productService.updateStatus(dto);
        return AjaxResult.success(true);
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public AjaxResult add(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return AjaxResult.success(true);
    }
}
