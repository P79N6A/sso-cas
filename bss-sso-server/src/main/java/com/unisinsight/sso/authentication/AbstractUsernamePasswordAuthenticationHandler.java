package com.unisinsight.sso.authentication;

import com.unisinsight.sso.authentication.handler.RememberMeUsernamePasswordCaptchaAuthenticationHandler;
import com.unisinsight.sso.exception.UsernamePasswordException;
import com.unisinsight.sso.service.UserService;
import com.unisinsight.sso.service.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.GeneralSecurityException;

/**
 * @Author: yangxiaoyu
 * @Date: 2019/7/11 10:49
 */
public abstract class AbstractUsernamePasswordAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    public AbstractUsernamePasswordAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    protected static final Logger LOGGER = LoggerFactory.getLogger(RememberMeUsernamePasswordCaptchaAuthenticationHandler.class);

    protected PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    protected UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(final Credential credential) throws GeneralSecurityException, PreventedException {
        final UsernamePasswordCredential originalUserPass = (UsernamePasswordCredential) credential;
        final UsernamePasswordCredential userPass = new UsernamePasswordCredential(originalUserPass.getUsername(), originalUserPass.getPassword());
        if (StringUtils.isBlank(userPass.getUsername()) || StringUtils.isBlank(userPass.getPassword())) {
            throw new UsernamePasswordException("用户名或密码错误！");
        }
        final String transformedPsw = this.passwordEncoder.encode(userPass.getPassword());
        LOGGER.debug("用户密码加密器：{},加密后密码:{}", this.passwordEncoder.getClass().getName(), transformedPsw);

        User user = userService.findByUserName(userPass.getUsername());

        if (user == null || !user.getPassword().equalsIgnoreCase(transformedPsw)) {
            throw new UsernamePasswordException("用户名或密码错误！");
        }
        /*if (Constants.ONE == user.getDisabled()) {
            throw new AccountDisableException("账号已被锁定,请联系管理员！");
        }
        if (Constants.ONE == user.getExpired()) {
            throw new AccountExpireException("密码已过期,请修改密码！");
        }*/
        return authenticateUsernamePasswordInternal(credential, user);
    }

    protected abstract AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(Credential credential, User user) throws GeneralSecurityException, PreventedException;

}
