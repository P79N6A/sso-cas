package com.unisinsight.sso.demo.service;


import com.unisinsight.sso.demo.dao.LoginDao;
import com.unisinsight.sso.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    public boolean verifyLogin(User user) {

        List<User> userList = loginDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        return userList.size() > 0;
    }

}