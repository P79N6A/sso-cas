package com.unisinsight.sso.webflow;

import com.unisinsight.sso.service.entity.RememberMeUsernamePasswordCaptchaCredential;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.DefaultLoginWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * 新增验证码校验
 * @author: yangxiaoyu
 * @description: 重新定义 Credential model
 */
public class DefaultCaptchaWebflowConfigurer extends DefaultLoginWebflowConfigurer {
    /**
     * Instantiates a new Default webflow configurer.
     *
     * @param flowBuilderServices    the flow builder services
     * @param flowDefinitionRegistry the flow definition registry
     * @param applicationContext     the application context
     * @param casProperties          the cas properties
     */
    public DefaultCaptchaWebflowConfigurer(FlowBuilderServices flowBuilderServices, FlowDefinitionRegistry flowDefinitionRegistry, ApplicationContext applicationContext, CasConfigurationProperties casProperties) {
        super(flowBuilderServices, flowDefinitionRegistry, applicationContext, casProperties);
    }


    /**
     * Create remember me authn webflow config.
     *
     * @param flow the flow
     */
    @Override
    protected void createRememberMeAuthnWebflowConfig(Flow flow) {
        final ViewState state = getState(flow, CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM, ViewState.class);
        final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
        //使用 RememberMe credential
        if (casProperties.getTicket().getTgt().getRememberMe().isEnabled()) {
            createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, RememberMeUsernamePasswordCaptchaCredential.class);
            // rememberMe绑定
            cfg.addBinding(new BinderConfiguration.Binding("rememberMe", null, true));
            // 验证码绑定
            cfg.addBinding(new BinderConfiguration.Binding("captcha", null, true));
        } else {
            //使用 UsernamePassword credential
            createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, RememberMeUsernamePasswordCaptchaCredential.class);
            // 不绑定
            cfg.addBinding(new BinderConfiguration.Binding("rememberMe", null, false));
            cfg.addBinding(new BinderConfiguration.Binding("captcha", null, false));
        }
    }
}
