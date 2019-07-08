package com.unisinsight.sso.config;

import com.unisinsight.sso.authentication.RememberMeUsernamePasswordCaptchaAuthenticationHandler;
import com.unisinsight.sso.service.UserService;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置表单处理器
 *
 * @author: yangxiaoyu
 * @description:
 */
@Configuration("rememberMeConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class RememberMeConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Autowired
    private UserService userService;


    /**
     * 验证验证码，可通过order调整顺序
     *
     * @return
     */
    @Bean
    public AuthenticationHandler rememberMeUsernamePasswordCaptchaAuthenticationHandler() {
        RememberMeUsernamePasswordCaptchaAuthenticationHandler handler = new RememberMeUsernamePasswordCaptchaAuthenticationHandler(
                RememberMeUsernamePasswordCaptchaAuthenticationHandler.class.getSimpleName(),
                servicesManager,
                new DefaultPrincipalFactory(),
                100);
        handler.setUserService(userService);
        return handler;

       /* return new CustomerHandlerAuthentication(CustomerHandlerAuthentication.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1);*/
    }

    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(rememberMeUsernamePasswordCaptchaAuthenticationHandler());
    }
}
