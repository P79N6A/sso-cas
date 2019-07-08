package com.unisinsight.sso.config;


import com.unisinsight.sso.service.controller.CaptchaController;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @deprecated
 * 控制器配置类
 * @author yangxiaoyu
 */
@Configuration("captchaConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CustomControllerConfigureration {

    /**
     * 验证码配置,注入bean到spring中
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "captchaController")
    public CaptchaController captchaController() {
        return new CaptchaController();
    }


   /* @Bean
    @ConditionalOnMissingBean(name = "serviceController")
    public ServiceController serviceController() {
        return new ServiceController();
    }*/
}
