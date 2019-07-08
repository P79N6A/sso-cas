package com.unisinsight.sso.service;

import com.unisinsight.sso.service.entity.RestUser;
import com.unisinsight.sso.service.entity.User;

/**
 * @author: wangsaichao
 * @date: 2018/7/19
 * @description:
 */
public interface UserService {

    User findByUserName(String userName);

    RestUser findUserByUserName(String userName);
}
