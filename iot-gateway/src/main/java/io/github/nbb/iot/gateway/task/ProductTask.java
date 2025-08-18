package io.github.nbb.iot.gateway.task;

import cn.hutool.core.util.ReUtil;
import io.github.nbb.iot.common.domain.IotDeviceDO;
import io.github.nbb.iot.common.domain.IotProductDO;
import io.github.nbb.iot.common.domain.IotSerialDO;
import io.github.nbb.iot.gateway.framework.netty.NettyConnectionManager;
import io.github.nbb.iot.gateway.store.DeviceStore;
import io.github.nbb.iot.gateway.store.ProductStore;
import io.github.nbb.iot.gateway.store.SerialStore;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.janino.SimpleCompiler;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 胡鹏
 */
@Slf4j
@Component
public class ProductTask implements Job {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private DeviceStore deviceStore;
    @Autowired
    private SerialStore serialStore;
    @Autowired
    private ProductStore productStore;
    @Autowired
    private NettyConnectionManager nettyConnectionManager;

    private static final String PRODUCT_ID_KEY = "PRODUCT_ID";


    public void reloadTask(List<IotProductDO> productList) {
        for (IotProductDO productDO : productList) {
            try {
                Long productId = productDO.getId();
                SimpleTrigger trigger = this.getTrigger(productId);

                if (null == trigger) {
                    // 新建任务
                    JobDetail jobDetail = this.genJobDetail(productId);
                    SimpleTrigger newTrigger = this.genTrigger(productId, productDO.getCollectInterval());
                    scheduler.scheduleJob(jobDetail, newTrigger);
                    continue;
                }
                if (trigger.getRepeatInterval() != productDO.getCollectInterval().longValue()) {
                    // 采集间隔变化，更新采集间隔
                    SimpleTrigger newTrigger = this.genTrigger(productId, productDO.getCollectInterval());
                    scheduler.rescheduleJob(this.genTriggerKey(productId), newTrigger);
                    continue;
                }
                // 采集间隔相同，则跳过不处理
            } catch (SchedulerException e) {
                log.error("产品id为{}的设备扫描任务加载失败", productDO.getId());
                log.error(e.getMessage(), e);
            }

        }
    }

    private SimpleTrigger getTrigger(Long productId) throws SchedulerException {
        return (SimpleTrigger) scheduler.getTrigger(this.genTriggerKey(productId));
    }

    private SimpleTrigger genTrigger(Long productId, Integer intervalSeconds) {
        // 创建触发器
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(String.valueOf(productId), "productGroup")
                .startNow() // 立即开始
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(intervalSeconds) // 间隔30秒
                        .repeatForever()) // 无限重复
                .build();
        return trigger;
    }
    private TriggerKey genTriggerKey(Long productId) {
        return TriggerKey.triggerKey(String.valueOf(productId), "productGroup");
    }

    private JobDetail genJobDetail(Long productId) {
        JobDetail jobDetail = JobBuilder.newJob(ProductTask.class)
                .withIdentity(String.valueOf(productId), "productGroup")
                .build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(PRODUCT_ID_KEY, productId);
        return jobDetail;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Long productId = (Long) jobExecutionContext.getJobDetail().getJobDataMap().get(PRODUCT_ID_KEY);
        log.info("产品id为{}的设备扫描任务开始执行", productId);

        IotProductDO productDO = productStore.getById(productId);
        // 1、编译动态脚本类
        Object dynamicClientInstance;
        Method dynamicClientMethod;
        try {
            SimpleCompiler compiler = new SimpleCompiler();
            compiler.cook(productDO.getDynamicCode());
            String className = this.getFullClassName(productDO.getDynamicCode());
            Class<?> dynamicClass = compiler.getClassLoader().loadClass(className);
            dynamicClientInstance = dynamicClass.newInstance();
            dynamicClientMethod = dynamicClass.getMethod(DYNAMIC_CODE_METHOD_NAME, String.class, int.class, String.class);
        } catch (Exception e) {
            log.error("产品id为{}的脚本加载失败", productId);
            return;
        }


        // 2、获取产品下的所有设备
        List<IotDeviceDO> deviceList = deviceStore.listByProductId(productId);

        for (IotDeviceDO deviceDO : deviceList) {
            // 3、获取设备所绑定的串口
            IotSerialDO serial = serialStore.getBySerialId(deviceDO.getSerialId());
            try {
                // 4. 给设备发送消息
                String response = (String) dynamicClientMethod.invoke(dynamicClientInstance, serial.getIp(), serial.getPort(), deviceDO.getSerialAddressCode());
                System.out.println(response);
            } catch (Exception e) {
                log.error("产品id为{}的设备扫描任务执行失败", productId);
                log.error(e.getMessage(), e);
            }
        }

    }



    private static final String DYNAMIC_CODE_METHOD_NAME = "sendMessage";

    private String getFullClassName(String dynamicCode) {

        // 1. 提取包名（匹配 package 后的内容，直到分号）
        String packageRegex = "package (.*?);";
        String packageName = ReUtil.get(packageRegex, dynamicCode, 1);

        // 2. 提取类名（匹配 public class 后的内容，直到空格）
        String classRegex = "public class (\\w+)\\s*.*";
        String className = ReUtil.get(classRegex, dynamicCode, 1);

        // 3. 拼接完整类名
        return packageName + "." + className;
    }
}
