package com.unisinsight.sso.service.impl;

import com.unisinsight.sso.service.dao.UserDao;
import com.unisinsight.sso.service.entity.RestUser;
import com.unisinsight.sso.service.entity.User;
import com.unisinsight.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: yangxiaoyu
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public RestUser findUserByUserName(String userName) {
        return userDao.findUserByUserName(userName);
    }
}
