package com.sample.modules.tAuthorizationConsent.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Li.HongKun
 * @since 2025-11-30 20:00:23
 */
@TableName("t_authorization_consent")
public class TAuthorizationConsent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("registered_client_id")
    private String registeredClientId;

    @TableField("principal_name")
    private String principalName;

    @TableField("authorities")
    private String authorities;

    public String getRegisteredClientId() {
        return registeredClientId;
    }

    public void setRegisteredClientId(String registeredClientId) {
        this.registeredClientId = registeredClientId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "TAuthorizationConsent{" +
            "registeredClientId = " + registeredClientId +
            ", principalName = " + principalName +
            ", authorities = " + authorities +
            "}";
    }
}
