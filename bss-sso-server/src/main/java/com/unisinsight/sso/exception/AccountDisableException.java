package com.unisinsight.sso.exception;

import javax.security.auth.login.AccountException;

/**
 * 用户被锁定
 * @author yangxiaoyu
 */
public class AccountDisableException extends AccountException  {


    public AccountDisableException(){
        super();
    }


    public AccountDisableException(String msg) {
        super(msg);
    }


}

