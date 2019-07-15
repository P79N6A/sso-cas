package com.unisinsight.sso.config;

import com.unisinsight.sso.authentication.handler.RememberMeUsernamePasswordCaptchaAuthenticationHandler;
import com.unisinsight.sso.authentication.handler.SampleUsernamePasswordAuthenticationHandler;
import com.unisinsight.sso.service.UserService;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.support.password.PasswordEncoderUtils;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.support.jdbc.QueryJdbcAuthenticationProperties;
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
        final QueryJdbcAuthenticationProperties properties = casProperties.getAuthn().getJdbc().getQuery().get(0);
        if(!casProperties.getTicket().getTgt().getRememberMe().isEnabled()){
            SampleUsernamePasswordAuthenticationHandler handler =  new SampleUsernamePasswordAuthenticationHandler(SampleUsernamePasswordAuthenticationHandler.class.getName(),
                     servicesManager, new DefaultPrincipalFactory(), 100);
            handler.setUserService(userService);
            handler.setPasswordEncoder(PasswordEncoderUtils.newPasswordEncoder(properties.getPasswordEncoder()));
            return handler;
        }
        RememberMeUsernamePasswordCaptchaAuthenticationHandler handler = new RememberMeUsernamePasswordCaptchaAuthenticationHandler(
                RememberMeUsernamePasswordCaptchaAuthenticationHandler.class.getSimpleName(),
                servicesManager,
                new DefaultPrincipalFactory(),
                100);
        handler.setUserService(userService);
        handler.setPasswordEncoder(PasswordEncoderUtils.newPasswordEncoder(properties.getPasswordEncoder()));
        return handler;


    }

    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(rememberMeUsernamePasswordCaptchaAuthenticationHandler());
    }
}
