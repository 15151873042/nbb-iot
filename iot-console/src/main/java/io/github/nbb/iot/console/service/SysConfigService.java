package io.github.nbb.iot.console.service;


import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.ConfigPageDTO;
import io.github.nbb.iot.console.domain.entity.SysConfig;

import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author ruoyi
 */
public interface SysConfigService {


    /**
     * 查询参数配置列表
     *
     * @param dto 参数配置信息
     * @return 参数配置集合
     */
    PageResult<SysConfig> listPage(ConfigPageDTO dto);

    /**
     * 查询参数配置信息
     *
     * @param id 参数配置ID
     * @return 参数配置信息
     */
    public SysConfig selectConfigById(Long id);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    public boolean selectCaptchaEnabled();

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public int insertConfig(SysConfig config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public int updateConfig(SysConfig config);

    /**
     * 批量删除参数信息
     *
     * @param ids 需要删除的参数ID
     */
    public void deleteConfigByIds(Long[] ids);

    /**
     * 加载参数缓存数据
     */
    public void loadingConfigCache();

    /**
     * 清空参数缓存数据
     */
    public void clearConfigCache();

    /**
     * 重置参数缓存数据
     */
    public void resetConfigCache();

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果
     */
    public boolean checkConfigKeyUnique(SysConfig config);


}
