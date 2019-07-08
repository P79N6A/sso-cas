package com.unisinsight.sso.exception;

import javax.security.auth.login.AccountException;

/**
 * 用户名密码错误
 * @author yangxiaoyu
 */
public class UsernamePasswordException extends AccountException  {


    public UsernamePasswordException(){
        super();
    }


    public UsernamePasswordException(String msg) {
        super(msg);
    }


}

