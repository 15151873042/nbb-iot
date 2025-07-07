package io.github.nbb.iot.console.domain.entity.iot;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.nbb.iot.console.domain.BaseEntity;
import lombok.*;

/**
 * 串口 DO
 *
 * @author 胡鹏
 */
@TableName("iot_serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotSerial extends BaseEntity {
    /**
     * ip
     */
    private String ip;
    /**
     * ip
     */
    private Integer port;
    /**
     * 是否在线
     */
    private Boolean isOnline;


}