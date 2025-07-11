package io.github.nbb.iot.console.service.iot;

import cn.hutool.core.thread.NamedThreadFactory;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.spring.boot.autoconfigure.Lock4jProperties;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class BasePublishToNacosService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements SmartInitializingSingleton {

    @Resource
    private Lock4jProperties lock4jProperties;
    @Resource
    private LockTemplate lockTemplate;
    @Resource
    private NacosConfigManager nacosConfigManager;


    private final ExecutorService pool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new NamedThreadFactory(getPublishDataId(), true),
            new ThreadPoolExecutor.DiscardOldestPolicy());


    @Override
    public void afterSingletonsInstantiated() {
        String lockKey = lock4jProperties.getLockKeyPrefix() + this.getPublishDataId();

        // 此处加锁的原因的是集群部署时，让一个服务推送配置信息到nacos
        LockInfo lockInfo = null;
        try {
            lockInfo = lockTemplate.lock(lockKey);
            if (null != lockInfo) {
                this.doPublish();
            }
        } finally {
            if (null != lockInfo) {
                final boolean releaseLock = lockTemplate.releaseLock(lockInfo);
                if (!releaseLock) {
                    log.error("releaseLock fail,lockKey={},lockValue={}", lockInfo.getLockKey(),
                            lockInfo.getLockValue());
                }
            }
        }
    }

    private void doPublish() {
        pool.execute(() -> {
            String publishDataId = getPublishDataId();
            String publicDataText = getPublishDataText();
            try {
                nacosConfigManager.getConfigService().publishConfig(publishDataId,  "DEFAULT_GROUP",  publicDataText, ConfigType.JSON.getType());
            } catch (NacosException e) {
                log.error("推送{}到nacos失败", publishDataId);
            }
        });
    }


    /**
     * 获取发布数据的文本内容。
     *
     * @return 返回包含发布数据的文本内容的字符串。
     */
    protected abstract String getPublishDataText();

    /**
     * 获取发布数据的唯一标识符。
     *
     * @return 返回与发布数据相关联的唯一标识符字符串。
     */
    protected abstract String getPublishDataId();
}
