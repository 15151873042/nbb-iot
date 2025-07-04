package io.github.nbb.iot.console.mapper;

import io.github.nbb.iot.console.domain.entity.SysDictData;
import io.github.nbb.iot.console.framework.mybatisplus.BaseMapperX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author ruoyi
 */
public interface SysDictDataMapper extends BaseMapperX<SysDictData> {
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 根据字典数据ID查询信息
     *
     * @param id 字典数据ID
     * @return 字典数据
     */
    public SysDictData selectDictDataById(Long id);

    /**
     * 查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据
     */
    public int countDictDataByType(String dictType);

    /**
     * 通过字典ID删除字典数据信息
     *
     * @param id 字典数据ID
     * @return 结果
     */
    public int deleteDictDataById(Long id);

    /**
     * 批量删除字典数据信息
     *
     * @param ids 需要删除的字典数据ID
     * @return 结果
     */
    public int deleteDictDataByIds(Long[] ids);

    /**
     * 新增字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 修改字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(SysDictData dictData);

    /**
     * 同步修改字典类型
     *
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     * @return 结果
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
