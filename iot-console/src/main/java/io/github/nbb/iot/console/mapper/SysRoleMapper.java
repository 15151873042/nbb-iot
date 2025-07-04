package io.github.nbb.iot.console.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.nbb.iot.console.domain.entity.SysRole;
import io.github.nbb.iot.console.framework.mybatisplus.BaseMapperX;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysRoleMapper extends BaseMapperX<SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * 通过角色ID查询角色
     *
     * @param id 角色ID
     * @return 角色对象信息
     */
    public SysRole selectRoleById(Long id);

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    public SysRole checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 角色信息
     */
    public SysRole checkRoleKeyUnique(String roleKey);

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 通过角色ID删除角色
     *
     * @param id 角色ID
     * @return 结果
     */
    public int deleteRoleById(Long id);

    /**
     * 批量删除角色信息
     *
     * @param ids 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] ids);
}
