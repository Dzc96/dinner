package com.appointment.dinner.user.service;

import com.appointment.dinner.user.model.SysUser;

import java.util.List;

/**
 * @Author Tony
 * @Time 2019/1/15
 */
public interface TestService {
    List<SysUser> getUserList();

    List<SysUser> getUserPages(int offset, int limit);

    SysUser getUserById(Long userId);

    int updateUser(SysUser sysUser);

    int deleteUserById(Long userId);

    SysUser getUserByUser(SysUser sysUser);

    int addUser(SysUser sysUser);

}

