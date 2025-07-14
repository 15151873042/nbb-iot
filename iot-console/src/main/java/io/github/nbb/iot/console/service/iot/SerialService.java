package io.github.nbb.iot.console.service.iot;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.iot.SerialAddSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialEditSaveDTO;
import io.github.nbb.iot.console.domain.dto.iot.SerialPageDTO;
import io.github.nbb.iot.console.domain.entity.iot.IotSerial;
import io.github.nbb.iot.console.domain.vo.iot.SerialNameVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 网关 Service 接口
 *
 * @author 芋道源码
 */
public interface SerialService extends IService<IotSerial> {


    PageResult<IotSerial> listPage(SerialPageDTO dto);

    @Transactional
    void addSave(SerialAddSaveDTO dto);

    @Transactional
    void editSave(SerialEditSaveDTO dto);

    @Transactional
    void deleteById(Long id);

    List<SerialNameVO> listAllName();
}
