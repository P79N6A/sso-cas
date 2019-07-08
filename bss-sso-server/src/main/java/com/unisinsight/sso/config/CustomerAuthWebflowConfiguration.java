package com.unisinsight.sso.config;

import com.unisinsight.sso.webflow.CustomWebflowConfigurer;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.CasWebflowExecutionPlan;
import org.apereo.cas.web.flow.CasWebflowExecutionPlanConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * @deprecated
 * webflow流程表单绑定配置类注册
 * @author yangxiaoyu
 */
@Configuration("customerAuthWebflowConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CustomerAuthWebflowConfiguration implements CasWebflowExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("loginFlowRegistry")
    private FlowDefinitionRegistry loginFlowDefinitionRegistry;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FlowBuilderServices flowBuilderServices;



    /*@Bean
    @RefreshScope
    @ConditionalOnMissingBean(name = "validateLoginAction")
    public Action validateLoginAction() {
        ValidateLoginAction validateCaptchaAction = new ValidateLoginAction();
        return validateCaptchaAction;
    }*/

    @Bean
    public CasWebflowConfigurer CustomWebflowConfigureration() {
        // 实例化自定义的表单配置类
        final CustomWebflowConfigurer c = new CustomWebflowConfigurer(flowBuilderServices, loginFlowDefinitionRegistry,
                applicationContext, casProperties);
        // 初始化
        c.initialize();
        // 返回对象
        return c;
    }


    @Override
    public void configureWebflowExecutionPlan(final CasWebflowExecutionPlan plan) {
        plan.registerWebflowConfigurer(CustomWebflowConfigureration());
    }

}
