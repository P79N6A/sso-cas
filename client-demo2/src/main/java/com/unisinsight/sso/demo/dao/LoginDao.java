package com.unisinsight.sso.demo.dao;


import com.unisinsight.sso.demo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yangxiaoyu
 */
@Repository
public interface LoginDao extends CrudRepository<User, Long> {

    List<User> findByUsernameAndPassword(String name, String password);
}