package io.github.nbb.iot.console.mapper;


import io.github.nbb.iot.console.domain.entity.SysDictType;
import io.github.nbb.iot.console.framework.mybatisplus.BaseMapperX;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author ruoyi
 */
public interface SysDictTypeMapper extends BaseMapperX<SysDictType> {
    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * 根据字典类型ID查询信息
     *
     * @param id 字典类型ID
     * @return 字典类型
     */
    public SysDictType selectDictTypeById(Long id);

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * 通过字典ID删除字典信息
     *
     * @param id 字典ID
     * @return 结果
     */
    public int deleteDictTypeById(Long id);

    /**
     * 批量删除字典类型信息
     *
     * @param ids 需要删除的字典ID
     * @return 结果
     */
    public int deleteDictTypeByIds(Long[] ids);

    /**
     * 新增字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int insertDictType(SysDictType dictType);

    /**
     * 修改字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int updateDictType(SysDictType dictType);

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    public SysDictType checkDictTypeUnique(String dictType);
}
