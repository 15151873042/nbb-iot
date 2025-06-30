package io.github.nbb.iot.console.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.UserAllocatedPageDTO;
import io.github.nbb.iot.console.domain.entity.SysDept;
import io.github.nbb.iot.console.domain.entity.SysRole;
import io.github.nbb.iot.console.domain.entity.SysUser;
import io.github.nbb.iot.console.domain.entity.SysUserRole;
import io.github.nbb.iot.console.framework.mybatisplus.MPJLambdaWrapperX;
import io.github.nbb.iot.console.mapper.SysUserMapper;
import io.github.nbb.iot.console.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

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
}
