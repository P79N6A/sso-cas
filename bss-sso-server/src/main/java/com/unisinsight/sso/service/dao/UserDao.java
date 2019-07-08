package com.unisinsight.sso.service.dao;

import com.unisinsight.sso.service.entity.RestUser;
import com.unisinsight.sso.service.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author: yangxiaoyu
 * @description: 操作用户信息
 */
@Repository("userDao")
public class UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("${jdbc.ds.querySql}")
    private String querySql;

    /**
     * 通过用户名查询用户信息
     *
     * @param userName
     * @return
     */
    public User findByUserName(String userName) {
        /*List<User> info = null;
        try {
            info =  jdbcTemplate.query(querySql, new Object[]{userName}, new BeanPropertyRowMapper(User.class));
        } catch (Exception e) {
            LOGGER.error("据用户名[{}]，查询sql[{]],查询到用户信息出错,错误信息:{}", userName, querySql, e);
        }
        if(info == null || info.size() == 0){
            return null;
        }
        return info.get(0);*/
        return (User) findUser(userName, User.class);
    }

    public RestUser findUserByUserName(String userName) {
        return (RestUser) findUser(userName, RestUser.class);
    }

   private Object findUser(String queryParam, Class clazz){
        List info = null;
        try {
            info =  jdbcTemplate.query(querySql, new Object[]{queryParam}, new BeanPropertyRowMapper(clazz));
        } catch (Exception e) {
            LOGGER.error("据用户名[{}]，查询sql[{]],查询到用户信息出错,错误信息:{}", queryParam, querySql, e);
        }
        if(info == null || info.size() == 0){
            return null;
        }
        return info.get(0);
    }

}
