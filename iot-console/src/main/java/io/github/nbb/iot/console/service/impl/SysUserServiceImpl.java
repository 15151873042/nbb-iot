package io.github.nbb.iot.console.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.nbb.iot.console.constant.UserConstants;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.UserAllocatedPageDTO;
import io.github.nbb.iot.console.domain.dto.UserPageDTO;
import io.github.nbb.iot.console.domain.entity.*;
import io.github.nbb.iot.console.framework.mybatisplus.MPJLambdaWrapperX;
import io.github.nbb.iot.console.framework.web.exception.ServiceException;
import io.github.nbb.iot.console.mapper.*;
import io.github.nbb.iot.console.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return getBaseMapper().selectUserByUserName(userName);
    }

    @Override
    public PageResult<SysUser> listAllocatedPage(UserAllocatedPageDTO dto) {
        MPJLambdaWrapperX<SysUser> query = new MPJLambdaWrapperX<>();
        query.leftJoin(SysDept.class, SysDept::getId, SysUser::getDeptId)
                .leftJoin(SysUserRole.class, SysUserRole::getUserId, SysUser::getId)
                .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)
                .eq(SysUser::getDelFlag, "0")
                .eq(SysRole::getId, dto.getRoleId())
                .likeIfExists(SysUser::getUserName, dto.getUserName())
                .likeIfExists(SysUser::getPhonenumber, dto.getPhonenumber());

        return sysUserMapper.selectJoinPage(dto, SysUser.class, query);
    }

    @Override
    public PageResult<SysUser> listUnallocatedPage(UserAllocatedPageDTO dto) {

        Page<SysUser> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        List<SysUser> userList = sysUserMapper.listUnallocatedPage(page, dto);
        return new PageResult<>(userList, page.getTotal());
    }

    @Override
    public PageResult<SysUser> listPage(UserPageDTO dto) {
        Page<SysUser> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        List<SysUser> userList = sysUserMapper.listPage(page, dto);
        return new PageResult<>(userList, page.getTotal());
    }

    @Override
    public SysUser selectUserById(Long id) {
        return sysUserMapper.selectUserById(id);
    }

    @Override
    public void checkUserAllowed(SysUser user) {
        if (ObjectUtil.isNotNull(user.getId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    @Override
    public boolean checkUserNameUnique(SysUser user) {
        Long userId = ObjectUtil.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = sysUserMapper.checkUserNameUnique(user.getUserName());
        if (ObjectUtil.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long userId = ObjectUtil.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = sysUserMapper.checkPhoneUnique(user.getPhonenumber());
        if (ObjectUtil.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long userId = ObjectUtil.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = sysUserMapper.checkEmailUnique(user.getEmail());
        if (ObjectUtil.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }


    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getId(), user.getRoleIds());
    }


    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (ObjectUtil.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }


    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (ObjectUtil.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }
}
