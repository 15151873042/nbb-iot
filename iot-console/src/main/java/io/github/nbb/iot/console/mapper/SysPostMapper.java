package io.github.nbb.iot.console.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.nbb.iot.console.domain.entity.SysMenu;
import io.github.nbb.iot.console.domain.entity.SysPost;
import io.github.nbb.iot.console.framework.mybatisplus.BaseMapperX;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author ruoyi
 */
public interface SysPostMapper extends BaseMapperX<SysPost> {
    /**
     * 查询岗位数据集合
     *
     * @param post 岗位信息
     * @return 岗位数据集合
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    public List<SysPost> selectPostAll();

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param id 岗位ID
     * @return 角色对象信息
     */
    public SysPost selectPostById(Long id);

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    public List<Long> selectPostListByUserId(Long userId);

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    public List<SysPost> selectPostsByUserName(String userName);

    /**
     * 删除岗位信息
     *
     * @param id 岗位ID
     * @return 结果
     */
    public int deletePostById(Long id);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    public int deletePostByIds(Long[] ids);

    /**
     * 修改岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int updatePost(SysPost post);

    /**
     * 新增岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int insertPost(SysPost post);

    /**
     * 校验岗位名称
     *
     * @param postName 岗位名称
     * @return 结果
     */
    public SysPost checkPostNameUnique(String postName);

    /**
     * 校验岗位编码
     *
     * @param postCode 岗位编码
     * @return 结果
     */
    public SysPost checkPostCodeUnique(String postCode);
}
