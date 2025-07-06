package io.github.nbb.iot.console.domain.entity.iot;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.nbb.iot.console.domain.BaseEntity;
import lombok.*;

/**
 * 网关 DO
 *
 * @author 芋道源码
 */
@TableName("iot_gateway")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotGateway extends BaseEntity {
    /**
     * 网关编号
     */
    private String code;
    /**
     * 是否在线
     */
    private Boolean isOnline;


}