package io.github.nbb.iot.console.domain.entity.iot;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.nbb.iot.console.domain.BaseEntity;
import lombok.*;

/**
 *
 * @author 胡鹏
 */
@TableName("iot_device")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotDevice extends BaseEntity {
    /**
     * 设备名称
     */
    private String deviceName;

    private Long productId;

    private Long serialId;
}
