package com.unisinsight.sso.exception;

import javax.security.auth.login.AccountException;

/**
 * 验证码错误
 * @author yangxiaoyu
 */
public class CaptchaException extends AccountException  {


    public CaptchaException(){
        super();
    }


    public CaptchaException(String msg) {
        super(msg);
    }


}

