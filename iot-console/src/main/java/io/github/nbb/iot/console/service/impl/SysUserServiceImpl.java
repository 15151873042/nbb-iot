package io.github.nbb.iot.console.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.nbb.iot.console.domain.entity.SysUser;
import io.github.nbb.iot.console.mapper.SysUserMapper;
import io.github.nbb.iot.console.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

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
}
