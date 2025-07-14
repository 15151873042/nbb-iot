package io.github.nbb.iot.gateway.store;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

import static io.github.nbb.iot.common.constants.NacosConfigConstants.GROUP_NAME;


/**
 * @author 胡鹏
 */
@Slf4j
public abstract class BaseStore<T> implements InitializingBean {

    private static final int DEFAULT_TIMEOUT = 3000;

    @Resource
    private NacosConfigManager nacosConfigManager;

    private String dataId;

    private Class<T> receiveDataClass;


    public BaseStore(String dataId, Class<T> receiveDataClass) {
        this.dataId = dataId;
        this.receiveDataClass = receiveDataClass;
    }


    private final ExecutorService pool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new NamedThreadFactory(this.dataId, true),
            new ThreadPoolExecutor.DiscardOldestPolicy());


    @Override
    public void afterPropertiesSet() throws Exception {
        this.loadInitialConfig();
        this.initNacosListener();
    }

    private void initNacosListener() throws NacosException {
        nacosConfigManager.getConfigService().addListener(this.dataId, GROUP_NAME, new Listener() {
            @Override
            public Executor getExecutor() {
                return pool;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                receiveConfigData(configInfo);
            }
        });

    }

    private void loadInitialConfig() throws NacosException {
        String configData = nacosConfigManager.getConfigService().getConfig(this.dataId, GROUP_NAME, DEFAULT_TIMEOUT);
        if (null == configData) {
            log.warn("加载nacos配置{}的值为null，请检查配置内容", this.dataId);
        } else {
            this.receiveConfigData(configData);
        }
    }

    private void receiveConfigData(String configInfo) {
        List<T> dataList = JSONUtil.toList(configInfo, receiveDataClass);
        this.receiveDataList(dataList);
    }


    /**
     * 接收配置数据的抽象方法。
     * 当接收到配置信息时，该方法将被调用。
     *
     * @param dataList 配置信息。
     */
    abstract void receiveDataList(List<T> dataList);
}
