package com.sample.modules.tRegisteredClient.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

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


    /**
     *  保存
     * @return TRegisteredClient
     */
    private TRegisteredClient build(){
        Assert.hasText(this.getClientId(), "clientId cannot be empty");
        Assert.hasText(this.getAuthorizationGrantTypes(), "authorizationGrantTypes cannot be empty");
        if (this.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())) {
            Assert.hasText(this.getRedirectUris(), "redirectUris cannot be empty");
        }
        if (!org.springframework.util.StringUtils.hasText(this.getClientName())) {
            this.setClientName(this.getId());
        }
        if (StringUtils.isEmpty(this.getClientAuthenticationMethods())) {
            this.setClientAuthenticationMethods(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue());
        }
        if (StringUtils.isEmpty(this.getClientSettings())) {

            ClientSettings.Builder builder = ClientSettings.builder();
            if (isPublicClientType()) {
                // @formatter:off
                builder
                        .requireProofKey(true)
                        .requireAuthorizationConsent(true);
                // @formatter:on
            }
            this.setClientSettings( builder.build().toString());

        }
        if (StringUtils.isEmpty(this.getTokenSettings())) {
//            this.tokenSettings = TokenSettings.builder().build();
            this.setTokenSettings( TokenSettings.builder().build().toString());
        }

        return create();
    }

    private boolean isPublicClientType() {
//        return this.authorizationGrantTypes.contains(AuthorizationGrantType.AUTHORIZATION_CODE)
//                && this.clientAuthenticationMethods.size() == 1
//                && this.clientAuthenticationMethods.contains(ClientAuthenticationMethod.NONE);

        return this.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())
                && new HashSet<>(Arrays.asList(this.getClientAuthenticationMethods().split(","))).size() ==1
                && this.getClientAuthenticationMethods().contains(ClientAuthenticationMethod.NONE.getValue());
    }

    private TRegisteredClient create() {
        TRegisteredClient registeredClient = new TRegisteredClient();

        registeredClient.id = this.id;
        registeredClient.clientId = this.clientId;
        registeredClient.clientIdIssuedAt = this.clientIdIssuedAt;
        registeredClient.clientSecret = this.clientSecret;
        registeredClient.clientSecretExpiresAt = this.clientSecretExpiresAt;
        registeredClient.clientName = this.clientName;
        registeredClient.clientAuthenticationMethods = this.clientAuthenticationMethods;
        registeredClient.authorizationGrantTypes = this.authorizationGrantTypes;
        registeredClient.redirectUris = this.redirectUris;
        registeredClient.postLogoutRedirectUris =  this.postLogoutRedirectUris;
        registeredClient.scopes = this.scopes;
        registeredClient.clientSettings = this.clientSettings;
        registeredClient.tokenSettings = this.tokenSettings;
        return registeredClient;
    }

    private void validateScopes() {
        if (StringUtils.isEmpty(this.scopes)) {
            return;
        }

        for (String scope : new HashSet<>(Arrays.asList(this.scopes.split(",")))) {
            Assert.isTrue(validateScope(scope), "scope \"" + scope + "\" contains invalid characters");
        }
    }

    private static boolean validateScope(String scope) {
        return scope == null || scope.chars()
                .allMatch((c) -> withinTheRangeOf(c, 0x21, 0x21) || withinTheRangeOf(c, 0x23, 0x5B)
                        || withinTheRangeOf(c, 0x5D, 0x7E));
    }

    private static boolean withinTheRangeOf(int c, int min, int max) {
        return c >= min && c <= max;
    }


    private void validateRedirectUris() {
        if (StringUtils.isEmpty(this.redirectUris)) {
            return;
        }

        for (String redirectUri : new HashSet<>(Arrays.asList(this.redirectUris.split(",")))) {
            Assert.isTrue(validateRedirectUri(redirectUri),
                    "redirect_uri \"" + redirectUri + "\" is not a valid redirect URI or contains fragment");
        }
    }

    private void validatePostLogoutRedirectUris() {
        if (StringUtils.isEmpty(this.postLogoutRedirectUris)) {
            return;
        }

        for (String postLogoutRedirectUri : new HashSet<>(Arrays.asList(this.postLogoutRedirectUris.split(",")))) {
            Assert.isTrue(validateRedirectUri(postLogoutRedirectUri), "post_logout_redirect_uri \""
                    + postLogoutRedirectUri + "\" is not a valid post logout redirect URI or contains fragment");
        }
    }

    private static boolean validateRedirectUri(String redirectUri) {
        try {
            URI validRedirectUri = new URI(redirectUri);
            return validRedirectUri.getFragment() == null;
        }
        catch (URISyntaxException ex) {
            return false;
        }
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
