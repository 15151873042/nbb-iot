package io.github.nbb.iot.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.nbb.iot.console.core.domain.entity.SysUser;

/**
 * 用户表 数据层
 * 
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);
}
