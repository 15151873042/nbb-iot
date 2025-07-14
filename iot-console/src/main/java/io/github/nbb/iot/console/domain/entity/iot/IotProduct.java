package io.github.nbb.iot.console.domain.entity.iot;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.nbb.iot.console.domain.BaseEntity;
import lombok.*;

/**
 *
 * @author 胡鹏
 */
@TableName("iot_product")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotProduct extends BaseEntity {
    /**
     * 产品名称
     */
    private String productName;


}
