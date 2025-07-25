package io.github.nbb.iot.console.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.nbb.iot.console.constant.UserConstants;
import io.github.nbb.iot.console.domain.PageResult;
import io.github.nbb.iot.console.domain.dto.PostPageDTO;
import io.github.nbb.iot.console.domain.entity.SysPost;
import io.github.nbb.iot.console.framework.mybatisplus.LambdaQueryWrapperX;
import io.github.nbb.iot.console.framework.web.exception.ServiceException;
import io.github.nbb.iot.console.mapper.SysPostMapper;
import io.github.nbb.iot.console.mapper.SysUserPostMapper;
import io.github.nbb.iot.console.service.SysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {
    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Override
    public PageResult<SysPost> listPage(PostPageDTO dto) {
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapperX<SysPost>()
                .likeIfPresent(SysPost::getPostCode, dto.getPostCode())
                .likeIfPresent(SysPost::getPostName, dto.getPostName())
                .eqIfPresent(SysPost::getStatus, dto.getStatus())
                .orderByDesc(SysPost::getCreateTime);
        PageResult<SysPost> pageResult = postMapper.selectPage(dto, queryWrapper);
        return pageResult;
    }

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll() {
        return postMapper.selectPostAll();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param id 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long id) {
        return postMapper.selectPostById(id);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostNameUnique(SysPost post) {
        Long postId = ObjectUtil.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        if (ObjectUtil.isNotNull(info) && info.getId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        Long postId = ObjectUtil.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (ObjectUtil.isNotNull(info) && info.getId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deletePostById(Long postId) {
        return postMapper.deletePostById(postId);
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return postMapper.deletePostByIds(postIds);
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int insertPost(SysPost post) {
        return postMapper.insertPost(post);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int updatePost(SysPost post) {
        return postMapper.updatePost(post);
    }

}
