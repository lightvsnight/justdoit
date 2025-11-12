package com.sample.modules.tRegisteredClient.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Li.HongKun
 * @since 2025-11-12 20:18:22
 */
@TableName("t_registered_client")
public class TRegisteredClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("client_id")
    private String clientId;

    @TableField("client_id_issued_at")
    private Date clientIdIssuedAt;

    @TableField("client_secret")
    private String clientSecret;

    @TableField("client_secret_expires_at")
    private Date clientSecretExpiresAt;

    @TableField("client_name")
    private String clientName;

    @TableField("client_authentication_methods")
    private String clientAuthenticationMethods;

    @TableField("authorization_grant_types")
    private String authorizationGrantTypes;

    @TableField("redirect_uris")
    private String redirectUris;

    @TableField("post_logout_redirect_uris")
    private String postLogoutRedirectUris;

    @TableField("scopes")
    private String scopes;

    @TableField("client_settings")
    private String clientSettings;

    @TableField("token_settings")
    private String tokenSettings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(Date clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Date getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(Date clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    public void setPostLogoutRedirectUris(String postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }

    @Override
    public String toString() {
        return "TRegisteredClient{" +
            "id = " + id +
            ", clientId = " + clientId +
            ", clientIdIssuedAt = " + clientIdIssuedAt +
            ", clientSecret = " + clientSecret +
            ", clientSecretExpiresAt = " + clientSecretExpiresAt +
            ", clientName = " + clientName +
            ", clientAuthenticationMethods = " + clientAuthenticationMethods +
            ", authorizationGrantTypes = " + authorizationGrantTypes +
            ", redirectUris = " + redirectUris +
            ", postLogoutRedirectUris = " + postLogoutRedirectUris +
            ", scopes = " + scopes +
            ", clientSettings = " + clientSettings +
            ", tokenSettings = " + tokenSettings +
            "}";
    }
}
