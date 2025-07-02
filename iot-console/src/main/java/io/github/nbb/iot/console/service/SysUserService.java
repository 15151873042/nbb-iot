package io.github.nbb.iot.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.UserAllocatedPageDTO;
import io.github.nbb.iot.console.domain.dto.UserPageDTO;
import io.github.nbb.iot.console.domain.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param dto 用户信息
     * @return 用户信息集合信息
     */
    PageResult<SysUser> listAllocatedPage(UserAllocatedPageDTO dto);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param dto 用户信息
     * @return 用户信息集合信息
     */
    PageResult<SysUser> listUnallocatedPage(UserAllocatedPageDTO dto);

    /**
     * 根据条件分页查询用户列表
     *
     * @param dto 用户信息
     * @return 用户信息集合信息
     */
    PageResult<SysUser> listPage(UserPageDTO dto);

    /**
     * 通过用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long id);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkEmailUnique(SysUser user);


    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);


    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);
}
