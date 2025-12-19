package com.sample.modules.tAuthorization.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.modules.tAuthorization.entity.TAuthorization;
import com.sample.modules.tAuthorization.mapper.TAuthorizationMapper;
import com.sample.modules.tAuthorization.service.TAuthorizationService;
import com.sample.modules.tRegisteredClient.service.TRegisteredClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Li.HongKun
 * @since 2025-11-30 19:46:59
 */
@Service
public class TAuthorizationServiceImpl extends ServiceImpl<TAuthorizationMapper, TAuthorization> implements TAuthorizationService, OAuth2AuthorizationService {

    @Autowired
    private TRegisteredClientService tRegisteredClientService;

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        OAuth2Authorization existingAuthorization = this.findById(authorization.getId());
        if (existingAuthorization == null) {
//            this.insertAuthorization(authorization);
            // 将OAuth2Authorization转换为自己的结构表TAuthorization
            TAuthorization tAuthorization =  new TAuthorization();
//            tAuthorization.setId(UUID.randomUUID().toString());
            tAuthorization.setId(authorization.getId());
            this.builder(authorization,tAuthorization);
            this.save(tAuthorization);
        } else {
//            this.updateAuthorization(authorization);
            TAuthorization tAuthorization =  new TAuthorization();
            tAuthorization.setId(authorization.getId());
            this.builder(authorization,tAuthorization);
            this.updateById(tAuthorization);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        Assert.hasText(authorization.getId(),"ID 不能为空");

        LambdaQueryChainWrapper<TAuthorization> queryWrapper =  this.lambdaQuery();
        queryWrapper.eq(TAuthorization::getId,authorization.getId());
        super.remove(queryWrapper);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        TAuthorization authorization = this.getById(id);
        if(authorization == null){
            return null;
        }else{
            return builder(authorization);
        }

    }

    /**
     * 2025年12月17日 计划重写此方法
     */
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        TAuthorization tAuthorization = new TAuthorization();
        if (tokenType == null) {
            tAuthorization.setState(token);
            this.lambdaQuery(tAuthorization);
        } else if ("state".equals(tokenType.getValue())) {
            tAuthorization.setState(token);
            this.lambdaQuery(tAuthorization);
        }else if ("code".equals(tokenType.getValue())) {
            tAuthorization.setAuthorizationCodeValue(token.getBytes(StandardCharsets.UTF_8));
            this.lambdaQuery(tAuthorization);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            tAuthorization.setAccessTokenValue(token.getBytes(StandardCharsets.UTF_8));
            this.lambdaQuery(tAuthorization);
        } else if ("id_token".equals(tokenType.getValue())) {
            tAuthorization.setOidcIdTokenValue(token.getBytes(StandardCharsets.UTF_8));
            this.lambdaQuery(tAuthorization);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            //parameters.add(mapToSqlParameter("refresh_token_value", token));
            //return this.findBy("refresh_token_value = ?", parameters);
            tAuthorization.setRefreshTokenValue(token.getBytes(StandardCharsets.UTF_8));
            this.lambdaQuery(tAuthorization);
        } else if ("user_code".equals(tokenType.getValue())) {
            //parameters.add(mapToSqlParameter("user_code_value", token));
            //return this.findBy("user_code_value = ?", parameters);
            tAuthorization.setUserCodeValue(token.getBytes(StandardCharsets.UTF_8));
            this.lambdaQuery(tAuthorization);
        } else if ("device_code".equals(tokenType.getValue())) {
            //parameters.add(mapToSqlParameter("device_code_value", token));
            //return this.findBy("device_code_value = ?", parameters);
            tAuthorization.setDeviceCodeValue(token.getBytes(StandardCharsets.UTF_8));
            this.lambdaQuery(tAuthorization);
        } else {
            return null;
        }

        return builder(tAuthorization);
    }

    OAuth2Authorization builder(TAuthorization tAuthorization){

        RegisteredClient registeredClient = tRegisteredClientService.findById(tAuthorization.getRegisteredClientId());

        if (registeredClient == null) {
            throw new DataRetrievalFailureException("The RegisteredClient with id '" + tAuthorization.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
        } else {
            OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient);

            String id = tAuthorization.getId();
            String principalName = tAuthorization.getPrincipalName();
            String authorizationGrantType =tAuthorization.getAuthorizationGrantType();
            Set<String> authorizedScopes = Collections.emptySet();
            String authorizedScopesString = tAuthorization.getAuthorizedScopes();
            if (authorizedScopesString != null) {
                // 将字符串，通过逗号分割为数组
                authorizedScopes = StringUtils.commaDelimitedListToSet(authorizedScopesString);
            }
            builder.id(id).principalName(principalName).authorizationGrantType(new AuthorizationGrantType(authorizationGrantType)).authorizedScopes(authorizedScopes);

            String attributesStr = new String(tAuthorization.getAttributes(), StandardCharsets.UTF_8);
            try {
                // TypeReference 解决泛型擦除问题
                Map<String, Object> attributes = new ObjectMapper().readValue(attributesStr, new TypeReference<Map<String, Object>>() {});
                builder.attributes((attrs) ->{
                    attrs.putAll(attributes);
                });
            } catch (Exception e) {
                throw new RuntimeException("JSON 解析失败：" + attributesStr, e);
            }


            String state = tAuthorization.getState();
            if (StringUtils.hasText(state)) {
                builder.attribute("state", state);
            }

            String authorizationCodeValue = new String(tAuthorization.getAuthorizationCodeValue(), StandardCharsets.UTF_8);
            Instant tokenIssuedAt;
            Instant tokenExpiresAt;
            if (StringUtils.hasText(authorizationCodeValue)) {
                tokenIssuedAt = tAuthorization.getAuthorizationCodeIssuedAt().toInstant();
                tokenExpiresAt = tAuthorization.getAuthorizationCodeExpiresAt().toInstant();
                OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(authorizationCodeValue, tokenIssuedAt, tokenExpiresAt);
                String authorizationCodeMetadataStr =  new String(tAuthorization.getAuthorizationCodeMetadata(), StandardCharsets.UTF_8);;
                try {
                    // TypeReference 解决泛型擦除问题
                    Map<String, Object> authorizationCodeMetadata = new ObjectMapper().readValue(authorizationCodeMetadataStr, new TypeReference<Map<String, Object>>() {});
                    builder.token(authorizationCode, (metadata) -> {
                        metadata.putAll(authorizationCodeMetadata);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("JSON 解析失败：" + authorizationCodeMetadataStr, e);
                }
            }

            String accessTokenValue = new String(tAuthorization.getAccessTokenValue(), StandardCharsets.UTF_8);
            if (StringUtils.hasText(accessTokenValue)) {
                tokenIssuedAt = tAuthorization.getAccessTokenIssuedAt().toInstant();
                tokenExpiresAt = tAuthorization.getAccessTokenExpiresAt().toInstant();

                OAuth2AccessToken.TokenType tokenType = null;
                if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(tAuthorization.getAccessTokenType())) {
                    tokenType = OAuth2AccessToken.TokenType.BEARER;
                } else if (OAuth2AccessToken.TokenType.DPOP.getValue().equalsIgnoreCase(tAuthorization.getAccessTokenType())) {
                    tokenType = OAuth2AccessToken.TokenType.DPOP;
                }

                Set<String> scopes = Collections.emptySet();
                String accessTokenScopes = tAuthorization.getAccessTokenScopes();
                if (accessTokenScopes != null) {
                    scopes = StringUtils.commaDelimitedListToSet(accessTokenScopes);
                }

                OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, accessTokenValue, tokenIssuedAt, tokenExpiresAt, scopes);
                String accessTokenMetadataStr =  new String(tAuthorization.getAccessTokenMetadata(), StandardCharsets.UTF_8);;
                try {
                    // TypeReference 解决泛型擦除问题
                    Map<String, Object> authorizationCodeMetadata = new ObjectMapper().readValue(accessTokenMetadataStr, new TypeReference<Map<String, Object>>() {});
                    builder.token(accessToken, (metadata) -> {
                        metadata.putAll(authorizationCodeMetadata);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("JSON 解析失败：" + accessTokenMetadataStr, e);
                }
            }

            //oidcIdToken
//            String oidcIdTokenValue = this.getLobValue(rs, "oidc_id_token_value");
            String oidcIdTokenValue = new String(tAuthorization.getOidcIdTokenValue(), StandardCharsets.UTF_8);
            if (StringUtils.hasText(oidcIdTokenValue)) {
                tokenIssuedAt = tAuthorization.getOidcIdTokenIssuedAt().toInstant();
                tokenExpiresAt = tAuthorization.getOidcIdTokenExpiresAt().toInstant();

                String oidcTokenMetadataStr =  new String(tAuthorization.getOidcIdTokenMetadata(), StandardCharsets.UTF_8);;
                try {
                    // TypeReference 解决泛型擦除问题
                    Map<String, Object> oidcTokenMetadata = new ObjectMapper().readValue(oidcTokenMetadataStr, new TypeReference<Map<String, Object>>() {});
                    OidcIdToken oidcToken = new OidcIdToken(oidcIdTokenValue, tokenIssuedAt, tokenExpiresAt, (Map)oidcTokenMetadata.get(OAuth2Authorization.Token.CLAIMS_METADATA_NAME));
                    builder.token(oidcToken, (metadata) -> {
                        metadata.putAll(oidcTokenMetadata);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("JSON 解析失败：" + oidcTokenMetadataStr, e);
                }
            }

            String refreshTokenValue = new String(tAuthorization.getRefreshTokenValue(), StandardCharsets.UTF_8);
            if (StringUtils.hasText(refreshTokenValue)) {
                tokenIssuedAt = tAuthorization.getRefreshTokenIssuedAt().toInstant();
                tokenExpiresAt = null;
                Date refreshTokenExpiresAt = tAuthorization.getRefreshTokenExpiresAt();
                if (refreshTokenExpiresAt != null) {
                    tokenExpiresAt = refreshTokenExpiresAt.toInstant();
                }

                String refreshTokenMetadataStr =  new String(tAuthorization.getRefreshTokenMetadata(), StandardCharsets.UTF_8);;
                try {
                    // TypeReference 解决泛型擦除问题
                    Map<String, Object> refreshTokenMetadata = new ObjectMapper().readValue(refreshTokenMetadataStr, new TypeReference<Map<String, Object>>() {});
                    OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue, tokenIssuedAt, tokenExpiresAt);
                    builder.token(refreshToken, (metadata) -> {
                        metadata.putAll(refreshTokenMetadata);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("JSON 解析失败：" + refreshTokenMetadataStr, e);
                }
            }



            String userCodeValue = new String(tAuthorization.getUserCodeValue(), StandardCharsets.UTF_8);
            if (StringUtils.hasText(userCodeValue)) {
                tokenIssuedAt = tAuthorization.getUserCodeIssuedAt().toInstant();
                tokenExpiresAt = tAuthorization.getUserCodeExpiresAt().toInstant();

                String userCodeMetadataStr =  new String(tAuthorization.getUserCodeMetadata(), StandardCharsets.UTF_8);;
                try {
                    // TypeReference 解决泛型擦除问题
                    Map<String, Object> userCodeMetadata = new ObjectMapper().readValue(userCodeMetadataStr, new TypeReference<Map<String, Object>>() {});

                    OAuth2UserCode userCode = new OAuth2UserCode(userCodeValue, tokenIssuedAt, tokenExpiresAt);

                    builder.token(userCode, (metadata) -> {
                        metadata.putAll(userCodeMetadata);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("JSON 解析失败：" + userCodeMetadataStr, e);
                }
            }



            String deviceCodeValue = new String(tAuthorization.getDeviceCodeValue(), StandardCharsets.UTF_8);
            if (StringUtils.hasText(deviceCodeValue)) {
                tokenIssuedAt = tAuthorization.getDeviceCodeIssuedAt().toInstant();
                tokenExpiresAt = tAuthorization.getDeviceCodeExpiresAt().toInstant();
                String deviceCodeMetadataStr =  new String(tAuthorization.getDeviceCodeMetadata(), StandardCharsets.UTF_8);;
                try {
                    // TypeReference 解决泛型擦除问题
                    Map<String, Object> deviceCodeMetadata = new ObjectMapper().readValue(deviceCodeMetadataStr, new TypeReference<Map<String, Object>>() {});
                    OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(deviceCodeValue, tokenIssuedAt, tokenExpiresAt);
                    builder.token(deviceCode, (metadata) -> {
                        metadata.putAll(deviceCodeMetadata);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("JSON 解析失败：" + deviceCodeMetadataStr, e);
                }

            }
            return builder.build();
        }
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception var3) {
            throw new IllegalArgumentException(var3.getMessage(), var3);
        }
    }

    /**
     * 2025年12月16日 构建了此方法
     */
    public void builder(OAuth2Authorization authorization, TAuthorization tAuthorization){

        //registeredClientId
        tAuthorization.setRegisteredClientId(authorization.getRegisteredClientId());
        //principalName
        tAuthorization.setPrincipalName(authorization.getPrincipalName());
        //authorizationGrantType 授权类型
        tAuthorization.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());

        //authorizedScopes
        String authorizedScopes = null;
        if (!CollectionUtils.isEmpty(authorization.getAuthorizedScopes())) {
            authorizedScopes = StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ",");
        }
        tAuthorization.setAuthorizedScopes(authorizedScopes);

        //attributes
        String attributes = this.writeMap(authorization.getAttributes());
        byte[] attributesByteUtf8 = attributes.getBytes(StandardCharsets.UTF_8);
        tAuthorization.setAttributes(attributesByteUtf8);

        //state
        String authorizationState = (String)authorization.getAttribute("state");
        String state = null;
        if (StringUtils.hasText(authorizationState)) {
            state = authorizationState;

        }
        tAuthorization.setState(state);

        //authorizationCodeValue
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        String codeValue = authorizationCode.getToken().getTokenValue();
        byte[] codeValueByteArrUtf8 = codeValue.getBytes(StandardCharsets.UTF_8);
        tAuthorization.setAuthorizationCodeValue(codeValueByteArrUtf8);

        //authorizationCodeIssuedAt
        authorizationCode.getToken().getIssuedAt();
        tAuthorization.setAuthorizationCodeIssuedAt(Date.from(authorizationCode.getToken().getIssuedAt()));
        //authorizationCodeExpiresAt
        tAuthorization.setAuthorizationCodeExpiresAt(Date.from(authorizationCode.getToken().getExpiresAt()));
        //authorizationCodeMetadata
        String authorizationCodeMetadata = this.writeMap(authorizationCode.getMetadata());
        byte[] metadataByteArrUtf8 = authorizationCodeMetadata.getBytes(StandardCharsets.UTF_8);
        tAuthorization.setAuthorizationCodeMetadata(metadataByteArrUtf8);


        //accessTokenValue
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);

        if (accessToken != null) {
            String tokenValue = accessToken.getToken().getTokenValue();
            byte[] tokenValueByteArrUtf8 = tokenValue.getBytes(StandardCharsets.UTF_8);
            tAuthorization.setAccessTokenValue(tokenValueByteArrUtf8);
            tAuthorization.setAccessTokenIssuedAt(Date.from(accessToken.getToken().getIssuedAt()));
            tAuthorization.setAccessTokenExpiresAt(Date.from(accessToken.getToken().getExpiresAt()));
            String accessTokenType = null;
            String accessTokenScopes = null;
            accessTokenType = ((OAuth2AccessToken)accessToken.getToken()).getTokenType().getValue();
            if (!CollectionUtils.isEmpty(((OAuth2AccessToken)accessToken.getToken()).getScopes())) {
                accessTokenScopes = StringUtils.collectionToDelimitedString(((OAuth2AccessToken)accessToken.getToken()).getScopes(), ",");
            }
            tAuthorization.setAccessTokenType(accessTokenType);
            tAuthorization.setAccessTokenScopes(accessTokenScopes);
            String accessTokenMetadata = this.writeMap(accessToken.getMetadata());
            byte[] accessTokenMetadataByteArrUtf8 = accessTokenMetadata.getBytes(StandardCharsets.UTF_8);
            tAuthorization.setAccessTokenMetadata(accessTokenMetadataByteArrUtf8);
        }


        //oidcIdToken
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if(oidcIdToken != null){
            String odicTokenValue = oidcIdToken.getToken().getTokenValue();
            tAuthorization.setOidcIdTokenValue(odicTokenValue.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setOidcIdTokenIssuedAt(Date.from(oidcIdToken.getToken().getIssuedAt()));
            tAuthorization.setOidcIdTokenExpiresAt(Date.from(oidcIdToken.getToken().getExpiresAt()));
            String oidcMetadata = this.writeMap(oidcIdToken.getMetadata());
            tAuthorization.setOidcIdTokenMetadata(oidcMetadata.getBytes(StandardCharsets.UTF_8));
        }


        //refreshTokenValue
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if(refreshToken != null){
           String refreshTokenValue =  refreshToken.getToken().getTokenValue();
           tAuthorization.setRefreshTokenValue(refreshTokenValue.getBytes(StandardCharsets.UTF_8));
           tAuthorization.setRefreshTokenIssuedAt(Date.from(refreshToken.getToken().getIssuedAt()));
           tAuthorization.setRefreshTokenExpiresAt(Date.from(refreshToken.getToken().getExpiresAt()));
           String refreshTokenMetadata = this.writeMap(oidcIdToken.getMetadata());
           tAuthorization.setRefreshTokenMetadata(refreshTokenMetadata.getBytes(StandardCharsets.UTF_8));
        }

        //userCodeValue
        OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
        if(userCode != null){
            String userCodeValue =  userCode.getToken().getTokenValue();
            tAuthorization.setUserCodeValue(userCodeValue.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setUserCodeIssuedAt(Date.from(userCode.getToken().getIssuedAt()));
            tAuthorization.setUserCodeExpiresAt(Date.from(userCode.getToken().getExpiresAt()));
            String userCodeMetadata = this.writeMap(userCode.getMetadata());
            tAuthorization.setDeviceCodeMetadata(userCodeMetadata.getBytes(StandardCharsets.UTF_8));
        }

        //deviceCodeValue
        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
        if(deviceCode != null){
            String deviceCodeValue =  deviceCode.getToken().getTokenValue();
            tAuthorization.setDeviceCodeValue(deviceCodeValue.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setDeviceCodeIssuedAt(Date.from(deviceCode.getToken().getIssuedAt()));
            tAuthorization.setDeviceCodeExpiresAt(Date.from(deviceCode.getToken().getExpiresAt()));
            String deviceCodeMetadata = this.writeMap(deviceCode.getMetadata());
            tAuthorization.setDeviceCodeMetadata(deviceCodeMetadata.getBytes(StandardCharsets.UTF_8));
        }
    }
}
