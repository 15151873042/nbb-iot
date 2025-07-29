import cn.hutool.core.util.HexUtil;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Test {

    public static void main(String[] args) {
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow() // 立即开始
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(30) // 间隔30秒
                        .repeatForever()) // 无限重复
                .build();

    }
}
