package io.github.nbb.iot.console.service;


import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.DictDataPageDTO;
import io.github.nbb.iot.console.domain.entity.SysDictData;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
public interface SysDictDataService {

    /**
     * 根据条件分页查询字典数据
     *
     * @param dto 查询参数对象
     * @return 字典数据集合信息
     */
    PageResult<SysDictData> listPage(DictDataPageDTO dto);

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * 根据字典数据ID查询信息
     *
     * @param id 字典数据ID
     * @return 字典数据
     */
    public SysDictData selectDictDataById(Long id);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    public void deleteDictDataByIds(Long[] dictCodes);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(SysDictData dictData);


}
