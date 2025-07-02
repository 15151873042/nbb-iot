package io.github.nbb.iot.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.nbb.iot.console.domain.dto.UserAllocatedPageDTO;
import io.github.nbb.iot.console.domain.dto.UserPageDTO;
import io.github.nbb.iot.console.domain.entity.SysUser;
import io.github.nbb.iot.console.framework.mybatisplus.BaseMapperX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 *
 */
public interface SysUserMapper extends BaseMapperX<SysUser> {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param dto 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> listUnallocatedPage(IPage<SysUser> page, @Param("dto") UserAllocatedPageDTO dto);

    /**
     * 根据条件分页查询用户列表
     *
     * @return 用户信息集合信息
     */
    List<SysUser> listPage(IPage<SysUser> page, @Param("dto") UserPageDTO dto);

    /**
     * 通过用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long id);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);



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
