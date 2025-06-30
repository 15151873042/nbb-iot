package io.github.nbb.iot.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.nbb.iot.console.domain.dto.UserAllocatedPageDTO;
import io.github.nbb.iot.console.domain.entity.SysUser;
import io.github.nbb.iot.console.framework.mybatisplus.BaseMapperX;

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
    List<SysUser> listUnallocatedPage(IPage<SysUser> page, UserAllocatedPageDTO dto);
}
