package com.unisinsight.sso.service.entity;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;

import javax.validation.constraints.Size;

/**
 * @author: yangxiaoyu
 * @description: 验证码 Credential
 */
public class RememberMeUsernamePasswordCaptchaCredential extends RememberMeUsernamePasswordCredential {

    @Size(min = 5,max = 5, message = "请输入验证码")
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.captcha)
                .toHashCode();
    }
}
