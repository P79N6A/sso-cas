package com.unisinsight.sso.authentication;

import com.unisinsight.sso.exception.AccountDisableException;
import com.unisinsight.sso.exception.AccountExpireException;
import com.unisinsight.sso.exception.CaptchaException;
import com.unisinsight.sso.exception.UsernamePasswordException;
import com.unisinsight.sso.service.UserService;
import com.unisinsight.sso.service.entity.RememberMeUsernamePasswordCaptchaCredential;
import com.unisinsight.sso.service.entity.User;
import com.unisinsight.sso.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 表单处理器
 *
 * @author: yangxiaoyu
 * @description:
 */
public class RememberMeUsernamePasswordCaptchaAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RememberMeUsernamePasswordCaptchaAuthenticationHandler.class);


    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public RememberMeUsernamePasswordCaptchaAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        RememberMeUsernamePasswordCaptchaCredential myCredential = (RememberMeUsernamePasswordCaptchaCredential) credential;
        String requestCaptcha = myCredential.getCaptcha();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object attribute = attributes.getRequest().getSession().getAttribute("captcha");

        String realCaptcha = attribute == null ? null : attribute.toString();

        if (StringUtils.isBlank(requestCaptcha) || !requestCaptcha.equals(realCaptcha)) {
            throw new CaptchaException("验证码错误");
        }

        String username = myCredential.getUsername();

        User user = userService.findByUserName(username);

        //可以在这里直接对用户名校验,或者调用 CredentialsMatcher 校验
        if (user == null || !user.getPassword().equals(myCredential.getPassword())) {
            //throw new UnknownAccountException("用户名或密码错误！");
            throw new UsernamePasswordException("用户名或密码错误！");
        }
        //这里将 密码对比 注销掉,否则 无法锁定  要将密码对比 交给 密码比较器 在这里可以添加自己的密码比较器等
        //if (!password.equals(user.getPassword())) {
        //    throw new IncorrectCredentialsException("用户名或密码错误！");
        //}
        if (Constants.ONE == user.getDisabled()) {
            throw new AccountDisableException("账号已被锁定,请联系管理员！");
        }
        if (Constants.ONE == user.getExpired()) {
            throw new AccountExpireException("密码已过期,请修改密码！");
        }
        // 可自定义返回给客户端的多个属性信息
        HashMap<String, Object> returnInfo = new HashMap<>();
        if (user != null) {
            returnInfo.put("expired", user.getExpired());
        }
        final List<MessageDescriptor> messageDescriptorList = new ArrayList<>();
        return createHandlerResult(credential, this.principalFactory.createPrincipal(username, returnInfo), messageDescriptorList);
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof RememberMeUsernamePasswordCaptchaCredential;
    }
}
