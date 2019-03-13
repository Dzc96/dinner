package com.appointment.dinner.user.service.impl;

import com.appointment.dinner.user.mapper.SysUserMapper;
import com.appointment.dinner.user.model.SysUser;
import com.appointment.dinner.user.service.TestService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Tony
 * @Time 2019/1/15
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> getUserList() {
        return sysUserMapper.selectAll();
    }

    @Override
    public List<SysUser> getUserPages(int offset, int limit) {
        PageHelper.startPage(offset,limit);
        return sysUserMapper.selectAll();
    }

    @Override
    public SysUser getUserById(Long userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateUser(SysUser sysUser) {
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int deleteUserById(Long userId) {
        return sysUserMapper.deleteByPrimaryKey(userId);
    }


    @Override
    public SysUser getUserByUser(SysUser user) {
        SysUser sysUser = sysUserMapper.selectOne(user);
        return user;
    }


}
