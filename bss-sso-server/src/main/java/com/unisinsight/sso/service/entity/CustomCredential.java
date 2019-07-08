
package com.unisinsight.sso.service.entity;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Size;

/**
 * @deprecated
 * 验证码实体类
 * @author yangxiaoyu
 */
public class CustomCredential extends UsernamePasswordCredential {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCredential.class);

    private static final long serialVersionUID = -4166149641561667276L;

    @Size(min = 6, max = 6, message = "验证码不能为空")
    private String capcha;

    public String getCapcha() {
        return capcha;
    }

    public void setCapcha(String capcha) {
        this.capcha = capcha;
    }

    public CustomCredential() {
    }

    public CustomCredential(final String capcha) {
        this.capcha = capcha;
    }


    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CustomCredential)) {
            return false;
        } else {
            CustomCredential other = (CustomCredential) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$capcha = this.capcha;
                Object other$capcha = other.capcha;
                if (this$capcha == null) {
                    if (other$capcha != null) {
                        return false;
                    }
                } else if (!this$capcha.equals(other$capcha)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CustomCredential;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.capcha)
                .toHashCode();
    }


}
