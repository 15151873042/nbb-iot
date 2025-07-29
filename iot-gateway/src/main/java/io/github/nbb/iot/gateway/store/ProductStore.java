package io.github.nbb.iot.gateway.store;

import io.github.nbb.iot.common.constants.NacosConfigConstants;
import io.github.nbb.iot.common.domain.IotDeviceDO;
import io.github.nbb.iot.common.domain.IotProductDO;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.java2d.pipe.SpanShapeRenderer.Simple;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author 胡鹏
 */
@Component
public class ProductStore extends BaseStore<IotProductDO> implements SmartInitializingSingleton, Job {

    @Autowired
    private Scheduler scheduler;

    private List<IotProductDO> PRODUCT_LIST = new CopyOnWriteArrayList<>();

    public ProductStore() {
        super(NacosConfigConstants.IOT_PRODUCT_DATA_ID, IotProductDO.class);
    }

    @Override
    void receiveDataList(List<IotProductDO> deviceList) {
        synchronized (this) {
            PRODUCT_LIST.clear();
            PRODUCT_LIST.addAll(deviceList);
        }
    }


    @SneakyThrows
    @Override
    public void afterSingletonsInstantiated() {

        for (IotProductDO productDO : PRODUCT_LIST) {
            Long productId = productDO.getId();
            SimpleTrigger trigger = this.getTrigger(productId);
            if (null != trigger) {
                if (trigger.getRepeatInterval() == productDO.getCollectInterval().longValue()) {
                    continue; // 采集间隔相同则跳过
                } else {
                    SimpleTrigger newTrigger = this.genTrigger(productId, productDO.getCollectInterval());
                    scheduler.rescheduleJob(this.genTriggerKey(productId), newTrigger);
                    continue;
                }
            } else {
                JobDetail jobDetail = this.genJobDetail(productId);
                SimpleTrigger newTrigger = this.genTrigger(productId, productDO.getCollectInterval());
                scheduler.scheduleJob(jobDetail, trigger);
            }

        }
        System.out.println(PRODUCT_LIST);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        
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
        return JobBuilder.newJob(ProductStore.class)
                .withIdentity(String.valueOf(productId), "productGroup")
                .build();
    }


}
