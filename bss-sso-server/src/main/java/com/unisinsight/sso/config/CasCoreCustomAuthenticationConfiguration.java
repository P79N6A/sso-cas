package com.unisinsight.sso.config;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.unisinsight.sso.authentication.PolicyBasedCustomAuthenticationManager;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationAttributeReleasePolicy;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationManager;
import org.apereo.cas.authentication.AuthenticationTransactionManager;
import org.apereo.cas.authentication.DefaultAuthenticationAttributeReleasePolicy;
import org.apereo.cas.authentication.DefaultAuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.DefaultAuthenticationTransactionManager;
import org.apereo.cas.authentication.PolicyBasedAuthenticationManager;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.core.authentication.AuthenticationAttributeReleaseProperties;
import org.apereo.cas.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 认证handler逻辑核心控制类(若需要修改核心逻辑可在spring.factories中将该文件加载，目前未加载)
 * @Author: yangxiaoyu
 * @Date: 2019/7/5 16:49
 */
@Configuration("casCoreCustomAuthenticationConfiguration")
@EnableConfigurationProperties({CasConfigurationProperties.class})
public class CasCoreCustomAuthenticationConfiguration {
    @Generated
    private static final Logger LOGGER = LoggerFactory.getLogger(org.apereo.cas.config.CasCoreAuthenticationConfiguration.class);
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CasConfigurationProperties casProperties;

    public CasCoreCustomAuthenticationConfiguration() {
    }

    @Bean
    public AuthenticationTransactionManager authenticationTransactionManager(@Qualifier("casAuthenticationManager") final AuthenticationManager authenticationManager) {
        return new DefaultAuthenticationTransactionManager(this.applicationEventPublisher, authenticationManager);
    }

    @ConditionalOnMissingBean(
            name = {"casAuthenticationManager"}
    )
    @Autowired
    @Bean
    public AuthenticationManager casAuthenticationManager(@Qualifier("authenticationEventExecutionPlan") final AuthenticationEventExecutionPlan authenticationEventExecutionPlan) {
        return new PolicyBasedCustomAuthenticationManager(authenticationEventExecutionPlan, this.casProperties.getPersonDirectory().isPrincipalResolutionFailureFatal(), this.applicationEventPublisher);
    }

    @ConditionalOnMissingBean(
            name = {"authenticationEventExecutionPlan"}
    )
    @Autowired
    @Bean
    public AuthenticationEventExecutionPlan authenticationEventExecutionPlan(final List<AuthenticationEventExecutionPlanConfigurer> configurers) {
        DefaultAuthenticationEventExecutionPlan plan = new DefaultAuthenticationEventExecutionPlan();
        configurers.forEach((c) -> {
            String name = StringUtils.removePattern(c.getClass().getSimpleName(), "\\$.+");
            LOGGER.debug("Configuring authentication execution plan [{}]", name);
            c.configureAuthenticationExecutionPlan(plan);
        });
        return plan;
    }

    @ConditionalOnMissingBean(
            name = {"authenticationAttributeReleasePolicy"}
    )
    @RefreshScope
    @Bean
    public AuthenticationAttributeReleasePolicy authenticationAttributeReleasePolicy() {
        AuthenticationAttributeReleaseProperties authenticationAttributeRelease = this.casProperties.getAuthn().getAuthenticationAttributeRelease();
        DefaultAuthenticationAttributeReleasePolicy policy = new DefaultAuthenticationAttributeReleasePolicy();
        policy.setAttributesToRelease(authenticationAttributeRelease.getOnlyRelease());
        Set<String> attributesToNeverRelease = CollectionUtils.wrapSet(new String[]{"credential", "org.apereo.cas.authentication.principal.REMEMBER_ME"});
        attributesToNeverRelease.addAll(authenticationAttributeRelease.getNeverRelease());
        policy.setAttributesToNeverRelease(attributesToNeverRelease);
        return policy;
    }
}

