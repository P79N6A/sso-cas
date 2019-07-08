package com.unisinsight.sso.exception;

import javax.security.auth.login.AccountException;

/**
 * 用户被锁定
 * @author yangxiaoyu
 */
public class AccountExpireException extends AccountException  {


    public AccountExpireException(){
        super();
    }


    public AccountExpireException(String msg) {
        super(msg);
    }


}

