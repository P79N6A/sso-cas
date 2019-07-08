package com.unisinsight.sso.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: yangxiaoyu
 * @description: cas-rest返回cas服务端信息
 */
public class RestUser {

    @JsonProperty("id")
    @NotNull
    private String username;

    /**
     * 需要返回实现org.apereo.cas.authentication.principal.Principal的类名接口
     */
    @JsonProperty("@class")
    private String clazz = "org.apereo.cas.authentication.principal.SimplePrincipal";


    @JsonProperty("attributes")
    private Map<String, Object> attributes = new HashMap<>();

    @JsonIgnore
    @NotNull
    private String password;

    /**
     * 用户状态,根据状态判断是否可用
     */
    @JsonIgnore
    private int disabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public RestUser addAttribute(String key, Object val) {
        getAttributes().put(key, val);
        return this;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }
}
