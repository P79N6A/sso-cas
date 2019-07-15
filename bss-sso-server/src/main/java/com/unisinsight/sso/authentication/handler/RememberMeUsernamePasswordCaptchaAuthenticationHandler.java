package com.unisinsight.sso.authentication.handler;

import com.unisinsight.sso.authentication.AbstractUsernamePasswordAuthenticationHandler;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class RememberMeUsernamePasswordCaptchaAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    public RememberMeUsernamePasswordCaptchaAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(Credential credential, User user) throws GeneralSecurityException, PreventedException {
        RememberMeUsernamePasswordCaptchaCredential myCredential = (RememberMeUsernamePasswordCaptchaCredential) credential;
        String requestCaptcha = myCredential.getCaptcha();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object attribute = attributes.getRequest().getSession().getAttribute(Constants.KEY_CAPTCHA);

        String realCaptcha = attribute == null ? null : attribute.toString();

        //验证码不区分大小写
        if (StringUtils.isBlank(requestCaptcha) || !requestCaptcha.equalsIgnoreCase(realCaptcha)) {
            throw new CaptchaException("验证码错误");
        }

        // 可自定义返回给客户端的多个属性信息
        HashMap<String, Object> returnInfo = new HashMap<>();
        if (user != null) {
            returnInfo.put("expired", user.getExpired());
        }
        final List<MessageDescriptor> messageDescriptorList = new ArrayList<>();
        return createHandlerResult(credential, this.principalFactory.createPrincipal(user.getUsername(), returnInfo), messageDescriptorList);
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof RememberMeUsernamePasswordCaptchaCredential;
    }
}
