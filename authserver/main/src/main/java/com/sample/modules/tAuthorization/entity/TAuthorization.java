package com.sample.modules.tAuthorization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2025-11-30 19:46:59
 */
@TableName("t_authorization")
public class TAuthorization implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    @TableField("registered_client_id")
    private String registeredClientId;

    @TableField("principal_name")
    private String principalName;

    @TableField("authorization_grant_type")
    private String authorizationGrantType;

    @TableField("authorized_scopes")
    private String authorizedScopes;

    @TableField("attributes")
    private byte[] attributes;

    @TableField("state")
    private String state;

    @TableField("authorization_code_value")
    private byte[] authorizationCodeValue;

    @TableField("authorization_code_issued_at")
    private Date authorizationCodeIssuedAt;

    @TableField("authorization_code_expires_at")
    private Date authorizationCodeExpiresAt;

    @TableField("authorization_code_metadata")
    private byte[] authorizationCodeMetadata;

    @TableField("access_token_value")
    private byte[] accessTokenValue;

    @TableField("access_token_issued_at")
    private Date accessTokenIssuedAt;

    @TableField("access_token_expires_at")
    private Date accessTokenExpiresAt;

    @TableField("access_token_metadata")
    private byte[] accessTokenMetadata;

    @TableField("access_token_type")
    private String accessTokenType;

    @TableField("access_token_scopes")
    private String accessTokenScopes;

    @TableField("oidc_id_token_value")
    private byte[] oidcIdTokenValue;

    @TableField("oidc_id_token_issued_at")
    private Date oidcIdTokenIssuedAt;

    @TableField("oidc_id_token_expires_at")
    private Date oidcIdTokenExpiresAt;

    @TableField("oidc_id_token_metadata")
    private byte[] oidcIdTokenMetadata;

    @TableField("refresh_token_value")
    private byte[] refreshTokenValue;

    @TableField("refresh_token_issued_at")
    private Date refreshTokenIssuedAt;

    @TableField("refresh_token_expires_at")
    private Date refreshTokenExpiresAt;

    @TableField("refresh_token_metadata")
    private byte[] refreshTokenMetadata;

    @TableField("user_code_value")
    private byte[] userCodeValue;

    @TableField("user_code_issued_at")
    private Date userCodeIssuedAt;

    @TableField("user_code_expires_at")
    private Date userCodeExpiresAt;

    @TableField("user_code_metadata")
    private byte[] userCodeMetadata;

    @TableField("device_code_value")
    private byte[] deviceCodeValue;

    @TableField("device_code_issued_at")
    private Date deviceCodeIssuedAt;

    @TableField("device_code_expires_at")
    private Date deviceCodeExpiresAt;

    @TableField("device_code_metadata")
    private byte[] deviceCodeMetadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public void setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
    }

    public String getAuthorizedScopes() {
        return authorizedScopes;
    }

    public void setAuthorizedScopes(String authorizedScopes) {
        this.authorizedScopes = authorizedScopes;
    }

    public byte[] getAttributes() {
        return attributes;
    }

    public void setAttributes(byte[] attributes) {
        this.attributes = attributes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public byte[] getAuthorizationCodeValue() {
        return authorizationCodeValue;
    }

    public void setAuthorizationCodeValue(byte[] authorizationCodeValue) {
        this.authorizationCodeValue = authorizationCodeValue;
    }

    public Date getAuthorizationCodeIssuedAt() {
        return authorizationCodeIssuedAt;
    }

    public void setAuthorizationCodeIssuedAt(Date authorizationCodeIssuedAt) {
        this.authorizationCodeIssuedAt = authorizationCodeIssuedAt;
    }

    public Date getAuthorizationCodeExpiresAt() {
        return authorizationCodeExpiresAt;
    }

    public void setAuthorizationCodeExpiresAt(Date authorizationCodeExpiresAt) {
        this.authorizationCodeExpiresAt = authorizationCodeExpiresAt;
    }

    public byte[] getAuthorizationCodeMetadata() {
        return authorizationCodeMetadata;
    }

    public void setAuthorizationCodeMetadata(byte[] authorizationCodeMetadata) {
        this.authorizationCodeMetadata = authorizationCodeMetadata;
    }

    public byte[] getAccessTokenValue() {
        return accessTokenValue;
    }

    public void setAccessTokenValue(byte[] accessTokenValue) {
        this.accessTokenValue = accessTokenValue;
    }

    public Date getAccessTokenIssuedAt() {
        return accessTokenIssuedAt;
    }

    public void setAccessTokenIssuedAt(Date accessTokenIssuedAt) {
        this.accessTokenIssuedAt = accessTokenIssuedAt;
    }

    public Date getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public void setAccessTokenExpiresAt(Date accessTokenExpiresAt) {
        this.accessTokenExpiresAt = accessTokenExpiresAt;
    }

    public byte[] getAccessTokenMetadata() {
        return accessTokenMetadata;
    }

    public void setAccessTokenMetadata(byte[] accessTokenMetadata) {
        this.accessTokenMetadata = accessTokenMetadata;
    }

    public String getAccessTokenType() {
        return accessTokenType;
    }

    public void setAccessTokenType(String accessTokenType) {
        this.accessTokenType = accessTokenType;
    }

    public String getAccessTokenScopes() {
        return accessTokenScopes;
    }

    public void setAccessTokenScopes(String accessTokenScopes) {
        this.accessTokenScopes = accessTokenScopes;
    }

    public byte[] getOidcIdTokenValue() {
        return oidcIdTokenValue;
    }

    public void setOidcIdTokenValue(byte[] oidcIdTokenValue) {
        this.oidcIdTokenValue = oidcIdTokenValue;
    }

    public Date getOidcIdTokenIssuedAt() {
        return oidcIdTokenIssuedAt;
    }

    public void setOidcIdTokenIssuedAt(Date oidcIdTokenIssuedAt) {
        this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt;
    }

    public Date getOidcIdTokenExpiresAt() {
        return oidcIdTokenExpiresAt;
    }

    public void setOidcIdTokenExpiresAt(Date oidcIdTokenExpiresAt) {
        this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt;
    }

    public byte[] getOidcIdTokenMetadata() {
        return oidcIdTokenMetadata;
    }

    public void setOidcIdTokenMetadata(byte[] oidcIdTokenMetadata) {
        this.oidcIdTokenMetadata = oidcIdTokenMetadata;
    }

    public byte[] getRefreshTokenValue() {
        return refreshTokenValue;
    }

    public void setRefreshTokenValue(byte[] refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
    }

    public Date getRefreshTokenIssuedAt() {
        return refreshTokenIssuedAt;
    }

    public void setRefreshTokenIssuedAt(Date refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
    }

    public Date getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    public void setRefreshTokenExpiresAt(Date refreshTokenExpiresAt) {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public byte[] getRefreshTokenMetadata() {
        return refreshTokenMetadata;
    }

    public void setRefreshTokenMetadata(byte[] refreshTokenMetadata) {
        this.refreshTokenMetadata = refreshTokenMetadata;
    }

    public byte[] getUserCodeValue() {
        return userCodeValue;
    }

    public void setUserCodeValue(byte[] userCodeValue) {
        this.userCodeValue = userCodeValue;
    }

    public Date getUserCodeIssuedAt() {
        return userCodeIssuedAt;
    }

    public void setUserCodeIssuedAt(Date userCodeIssuedAt) {
        this.userCodeIssuedAt = userCodeIssuedAt;
    }

    public Date getUserCodeExpiresAt() {
        return userCodeExpiresAt;
    }

    public void setUserCodeExpiresAt(Date userCodeExpiresAt) {
        this.userCodeExpiresAt = userCodeExpiresAt;
    }

    public byte[] getUserCodeMetadata() {
        return userCodeMetadata;
    }

    public void setUserCodeMetadata(byte[] userCodeMetadata) {
        this.userCodeMetadata = userCodeMetadata;
    }

    public byte[] getDeviceCodeValue() {
        return deviceCodeValue;
    }

    public void setDeviceCodeValue(byte[] deviceCodeValue) {
        this.deviceCodeValue = deviceCodeValue;
    }

    public Date getDeviceCodeIssuedAt() {
        return deviceCodeIssuedAt;
    }

    public void setDeviceCodeIssuedAt(Date deviceCodeIssuedAt) {
        this.deviceCodeIssuedAt = deviceCodeIssuedAt;
    }

    public Date getDeviceCodeExpiresAt() {
        return deviceCodeExpiresAt;
    }

    public void setDeviceCodeExpiresAt(Date deviceCodeExpiresAt) {
        this.deviceCodeExpiresAt = deviceCodeExpiresAt;
    }

    public byte[] getDeviceCodeMetadata() {
        return deviceCodeMetadata;
    }

    public void setDeviceCodeMetadata(byte[] deviceCodeMetadata) {
        this.deviceCodeMetadata = deviceCodeMetadata;
    }

    @Override
    public String toString() {
        return "TAuthorization{" +
            "id = " + id +
            ", registeredClientId = " + registeredClientId +
            ", principalName = " + principalName +
            ", authorizationGrantType = " + authorizationGrantType +
            ", authorizedScopes = " + authorizedScopes +
            ", attributes = " + attributes +
            ", state = " + state +
            ", authorizationCodeValue = " + authorizationCodeValue +
            ", authorizationCodeIssuedAt = " + authorizationCodeIssuedAt +
            ", authorizationCodeExpiresAt = " + authorizationCodeExpiresAt +
            ", authorizationCodeMetadata = " + authorizationCodeMetadata +
            ", accessTokenValue = " + accessTokenValue +
            ", accessTokenIssuedAt = " + accessTokenIssuedAt +
            ", accessTokenExpiresAt = " + accessTokenExpiresAt +
            ", accessTokenMetadata = " + accessTokenMetadata +
            ", accessTokenType = " + accessTokenType +
            ", accessTokenScopes = " + accessTokenScopes +
            ", oidcIdTokenValue = " + oidcIdTokenValue +
            ", oidcIdTokenIssuedAt = " + oidcIdTokenIssuedAt +
            ", oidcIdTokenExpiresAt = " + oidcIdTokenExpiresAt +
            ", oidcIdTokenMetadata = " + oidcIdTokenMetadata +
            ", refreshTokenValue = " + refreshTokenValue +
            ", refreshTokenIssuedAt = " + refreshTokenIssuedAt +
            ", refreshTokenExpiresAt = " + refreshTokenExpiresAt +
            ", refreshTokenMetadata = " + refreshTokenMetadata +
            ", userCodeValue = " + userCodeValue +
            ", userCodeIssuedAt = " + userCodeIssuedAt +
            ", userCodeExpiresAt = " + userCodeExpiresAt +
            ", userCodeMetadata = " + userCodeMetadata +
            ", deviceCodeValue = " + deviceCodeValue +
            ", deviceCodeIssuedAt = " + deviceCodeIssuedAt +
            ", deviceCodeExpiresAt = " + deviceCodeExpiresAt +
            ", deviceCodeMetadata = " + deviceCodeMetadata +
            "}";
    }
}
