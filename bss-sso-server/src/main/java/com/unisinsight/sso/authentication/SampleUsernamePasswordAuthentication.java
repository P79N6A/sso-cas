package com.unisinsight.sso.authentication;


import com.unisinsight.sso.exception.AccountDisableException;
import com.unisinsight.sso.exception.AccountExpireException;
import com.unisinsight.sso.exception.UsernamePasswordException;
import com.unisinsight.sso.service.UserService;
import com.unisinsight.sso.service.entity.RememberMeUsernamePasswordCaptchaCredential;
import com.unisinsight.sso.service.entity.User;
import com.unisinsight.sso.utils.Constants;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author yangxiaoyu
 * 简单用户名密码验证
 */
public class SampleUsernamePasswordAuthentication extends AbstractUsernamePasswordAuthenticationHandler {

    public SampleUsernamePasswordAuthentication(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }


    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(Credential credential, User user) throws GeneralSecurityException, PreventedException {
        UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
        // 可自定义返回给客户端的多个属性信息
        HashMap<String, Object> returnInfo = new HashMap<>();
        returnInfo.put("expired", user.getExpired());

        final List<MessageDescriptor> list = new ArrayList<>();

        return createHandlerResult(usernamePasswordCredential,
                this.principalFactory.createPrincipal(usernamePasswordCredential.getUsername(), returnInfo), list);
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof UsernamePasswordCredential;
    }
}
