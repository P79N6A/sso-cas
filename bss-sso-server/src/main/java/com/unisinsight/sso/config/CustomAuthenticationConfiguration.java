package com.unisinsight.sso.config;

import com.unisinsight.sso.authentication.handler.CustomerAuthenticationHandler;
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
 * @deprecated
 * 带验证码或简单用户名密码认证器配置
 * @author yangxiaoyu
 */
@Configuration("customAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CustomAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;


    @Bean
    public AuthenticationHandler myAuthenticationHandler() {
        // 参数: name, servicesManager, principalFactory, order
        // 定义为优先使用它进行认证
//        return new SampleUsernamePasswordAuthenticationHandler(SampleUsernamePasswordAuthenticationHandler.class.getName(),
//                servicesManager, new DefaultPrincipalFactory(), 1);

        return new CustomerAuthenticationHandler(CustomerAuthenticationHandler.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1);
    }

    /**
     * 注册验证器
     * @param plan
     */
    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(myAuthenticationHandler());
    }
}