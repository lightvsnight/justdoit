package com.sample.modules.tAuthorization.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nimbusds.jose.util.ByteUtils;
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

import java.io.IOException;
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
            tAuthorization.setId(UUID.randomUUID().toString());
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
        LambdaQueryChainWrapper<TAuthorization> queryWrapper =  this.lambdaQuery();
        if (tokenType == null) {

            queryWrapper.eq(TAuthorization::getAuthorizationCodeValue,token.getBytes(StandardCharsets.UTF_8));
            queryWrapper.eq(TAuthorization::getAccessTokenValue,token.getBytes(StandardCharsets.UTF_8));
            queryWrapper.eq(TAuthorization::getOidcIdTokenValue,token.getBytes(StandardCharsets.UTF_8));
            queryWrapper.eq(TAuthorization::getRefreshTokenValue,token.getBytes(StandardCharsets.UTF_8));
            queryWrapper.eq(TAuthorization::getUserCodeValue,token.getBytes(StandardCharsets.UTF_8));
            queryWrapper.eq(TAuthorization::getDeviceCodeValue,token.getBytes(StandardCharsets.UTF_8));

        } else if ("state".equals(tokenType.getValue())) {
            queryWrapper.eq(TAuthorization::getState,token.getBytes(StandardCharsets.UTF_8));
        }else if ("code".equals(tokenType.getValue())) {
            queryWrapper.eq(TAuthorization::getAuthorizationCodeValue,token.getBytes(StandardCharsets.UTF_8));
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            queryWrapper.eq(TAuthorization::getAccessTokenValue,token.getBytes(StandardCharsets.UTF_8));
        } else if ("id_token".equals(tokenType.getValue())) {
            queryWrapper.eq(TAuthorization::getOidcIdTokenValue,token.getBytes(StandardCharsets.UTF_8));
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            //parameters.add(mapToSqlParameter("refresh_token_value", token));
            queryWrapper.eq(TAuthorization::getRefreshTokenValue,token.getBytes(StandardCharsets.UTF_8));
            this.getOne(this.lambdaQuery(tAuthorization));
        } else if ("user_code".equals(tokenType.getValue())) {
            queryWrapper.eq(TAuthorization::getUserCodeValue,token.getBytes(StandardCharsets.UTF_8));
        } else if ("device_code".equals(tokenType.getValue())) {
            queryWrapper.eq(TAuthorization::getDeviceCodeValue,token.getBytes(StandardCharsets.UTF_8));
        } else {
            return null;
        }
        tAuthorization = queryWrapper.one();
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

            String attributesStr = tAuthorization.getAttributes();
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

            Instant tokenIssuedAt;
            Instant tokenExpiresAt;
            if(StringUtils.hasText(tAuthorization.getAuthorizationCodeValue())) {
                String authorizationCodeValue = tAuthorization.getAuthorizationCodeValue();

                if (StringUtils.hasText(authorizationCodeValue)) {
                    tokenIssuedAt = tAuthorization.getAuthorizationCodeIssuedAt().toInstant();
                    tokenExpiresAt = tAuthorization.getAuthorizationCodeExpiresAt().toInstant();
                    OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(authorizationCodeValue, tokenIssuedAt, tokenExpiresAt);
                    String authorizationCodeMetadataStr = tAuthorization.getAuthorizationCodeMetadata();
                    ;
                    try {
                        // TypeReference 解决泛型擦除问题
                        Map<String, Object> authorizationCodeMetadata = new ObjectMapper().readValue(authorizationCodeMetadataStr, new TypeReference<Map<String, Object>>() {
                        });
                        builder.token(authorizationCode, (metadata) -> {
                            metadata.putAll(authorizationCodeMetadata);
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("JSON 解析失败：" + authorizationCodeMetadataStr, e);
                    }
                }
            }
            if(StringUtils.hasText(tAuthorization.getAccessTokenValue())) {
                String accessTokenValue = tAuthorization.getAccessTokenValue();
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

                    assert tokenType != null;
                    OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, accessTokenValue, tokenIssuedAt, tokenExpiresAt, scopes);
                    String accessTokenMetadataStr =  tAuthorization.getAccessTokenMetadata();
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
            }

            //oidcIdToken
            if(StringUtils.hasText(tAuthorization.getOidcIdTokenValue())) {
                String oidcIdTokenValue = tAuthorization.getOidcIdTokenValue();
                if (StringUtils.hasText(oidcIdTokenValue)) {
                    tokenIssuedAt = tAuthorization.getOidcIdTokenIssuedAt().toInstant();
                    tokenExpiresAt = tAuthorization.getOidcIdTokenExpiresAt().toInstant();

                    String oidcTokenMetadataStr = tAuthorization.getOidcIdTokenMetadata();
                    ;
                    try {
                        // TypeReference 解决泛型擦除问题
                        Map<String, Object> oidcTokenMetadata = new ObjectMapper().readValue(oidcTokenMetadataStr, new TypeReference<Map<String, Object>>() {
                        });
                        OidcIdToken oidcToken = new OidcIdToken(oidcIdTokenValue, tokenIssuedAt, tokenExpiresAt, (Map) oidcTokenMetadata.get(OAuth2Authorization.Token.CLAIMS_METADATA_NAME));
                        builder.token(oidcToken, (metadata) -> {
                            metadata.putAll(oidcTokenMetadata);
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("JSON 解析失败：" + oidcTokenMetadataStr, e);
                    }
                }
            }

            if(StringUtils.hasText(tAuthorization.getRefreshTokenValue())) {
                String refreshTokenValue = tAuthorization.getRefreshTokenValue();
                if (StringUtils.hasText(refreshTokenValue)) {
                    tokenIssuedAt = tAuthorization.getRefreshTokenIssuedAt().toInstant();
                    tokenExpiresAt = null;
                    Date refreshTokenExpiresAt = tAuthorization.getRefreshTokenExpiresAt();
                    if (refreshTokenExpiresAt != null) {
                        tokenExpiresAt = refreshTokenExpiresAt.toInstant();
                    }

                    String refreshTokenMetadataStr = tAuthorization.getRefreshTokenMetadata();
                    ;
                    try {
                        // TypeReference 解决泛型擦除问题
                        Map<String, Object> refreshTokenMetadata = new ObjectMapper().readValue(refreshTokenMetadataStr, new TypeReference<Map<String, Object>>() {
                        });
                        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue, tokenIssuedAt, tokenExpiresAt);
                        builder.token(refreshToken, (metadata) -> {
                            metadata.putAll(refreshTokenMetadata);
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("JSON 解析失败：" + refreshTokenMetadataStr, e);
                    }
                }
            }

            if(StringUtils.hasText(tAuthorization.getUserCodeValue())) {

                String userCodeValue = tAuthorization.getUserCodeValue();
                if (StringUtils.hasText(userCodeValue)) {
                    tokenIssuedAt = tAuthorization.getUserCodeIssuedAt().toInstant();
                    tokenExpiresAt = tAuthorization.getUserCodeExpiresAt().toInstant();

                    String userCodeMetadataStr = tAuthorization.getUserCodeValue();
                    ;
                    try {
                        // TypeReference 解决泛型擦除问题
                        Map<String, Object> userCodeMetadata = new ObjectMapper().readValue(userCodeMetadataStr, new TypeReference<Map<String, Object>>() {
                        });

                        OAuth2UserCode userCode = new OAuth2UserCode(userCodeValue, tokenIssuedAt, tokenExpiresAt);

                        builder.token(userCode, (metadata) -> {
                            metadata.putAll(userCodeMetadata);
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("JSON 解析失败：" + userCodeMetadataStr, e);
                    }
                }
            }

            if(StringUtils.hasText(tAuthorization.getDeviceCodeValue())) {
                String deviceCodeValue = tAuthorization.getDeviceCodeValue();
                if (StringUtils.hasText(deviceCodeValue)) {
                    tokenIssuedAt = tAuthorization.getDeviceCodeIssuedAt().toInstant();
                    tokenExpiresAt = tAuthorization.getDeviceCodeExpiresAt().toInstant();
                    String deviceCodeMetadataStr = tAuthorization.getDeviceCodeMetadata();
                    ;
                    try {
                        // TypeReference 解决泛型擦除问题
                        Map<String, Object> deviceCodeMetadata = new ObjectMapper().readValue(deviceCodeMetadataStr, new TypeReference<Map<String, Object>>() {
                        });
                        OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(deviceCodeValue, tokenIssuedAt, tokenExpiresAt);
                        builder.token(deviceCode, (metadata) -> {
                            metadata.putAll(deviceCodeMetadata);
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("JSON 解析失败：" + deviceCodeMetadataStr, e);
                    }

                }
            }
            return builder.build();
        }
    }


    private String writeMap(Map<String, Object> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
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
        tAuthorization.setAttributes(attributes);

        //state
        String authorizationState = (String)authorization.getAttribute("state");
        String state = null;
        if (StringUtils.hasText(authorizationState)) {
            state = authorizationState;
            tAuthorization.setState(state);
        }


        //authorizationCodeValue
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if(authorizationCode !=  null ){
            String codeValue = authorizationCode.getToken().getTokenValue();
//            byte[] codeValueByteArrUtf8 = codeValue.getBytes(StandardCharsets.UTF_8);
            tAuthorization.setAuthorizationCodeValue(codeValue);

            //authorizationCodeIssuedAt
            authorizationCode.getToken().getIssuedAt();
            tAuthorization.setAuthorizationCodeIssuedAt(Date.from(authorizationCode.getToken().getIssuedAt()));
            //authorizationCodeExpiresAt
            tAuthorization.setAuthorizationCodeExpiresAt(Date.from(authorizationCode.getToken().getExpiresAt()));
            //authorizationCodeMetadata
            String authorizationCodeMetadata = this.writeMap(authorizationCode.getMetadata());
//            byte[] metadataByteArrUtf8 = authorizationCodeMetadata.getBytes(StandardCharsets.UTF_8);
            tAuthorization.setAuthorizationCodeMetadata(authorizationCodeMetadata);
        }


        //accessTokenValue
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);

        if (accessToken != null) {
            String tokenValue = accessToken.getToken().getTokenValue();
            byte[] tokenValueByteArrUtf8 = tokenValue.getBytes(StandardCharsets.UTF_8);
            tAuthorization.setAccessTokenValue(tokenValue);
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
//            byte[] accessTokenMetadataByteArrUtf8 = accessTokenMetadata.getBytes(StandardCharsets.UTF_8);
            tAuthorization.setAccessTokenMetadata(accessTokenMetadata);
        }


        //oidcIdToken
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if(oidcIdToken != null){
            String odicTokenValue = oidcIdToken.getToken().getTokenValue();
//            tAuthorization.setOidcIdTokenValue(odicTokenValue.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setOidcIdTokenValue(odicTokenValue);
            tAuthorization.setOidcIdTokenIssuedAt(Date.from(oidcIdToken.getToken().getIssuedAt()));
            tAuthorization.setOidcIdTokenExpiresAt(Date.from(oidcIdToken.getToken().getExpiresAt()));
            String oidcMetadata = this.writeMap(oidcIdToken.getMetadata());
//            tAuthorization.setOidcIdTokenMetadata(oidcMetadata.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setOidcIdTokenMetadata(oidcMetadata);
        }


        //refreshTokenValue
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if(refreshToken != null){
           String refreshTokenValue =  refreshToken.getToken().getTokenValue();
//           tAuthorization.setRefreshTokenValue(refreshTokenValue.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setRefreshTokenValue(refreshTokenValue);
           tAuthorization.setRefreshTokenIssuedAt(Date.from(refreshToken.getToken().getIssuedAt()));
           tAuthorization.setRefreshTokenExpiresAt(Date.from(refreshToken.getToken().getExpiresAt()));
           String refreshTokenMetadata = this.writeMap(oidcIdToken.getMetadata());
//           tAuthorization.setRefreshTokenMetadata(refreshTokenMetadata.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setRefreshTokenMetadata(refreshTokenMetadata);
        }


        //userCodeValue
        OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
        if(userCode != null){
            String userCodeValue =  userCode.getToken().getTokenValue();
//            tAuthorization.setUserCodeValue(userCodeValue.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setUserCodeValue(userCodeValue);
            tAuthorization.setUserCodeIssuedAt(Date.from(userCode.getToken().getIssuedAt()));
            tAuthorization.setUserCodeExpiresAt(Date.from(userCode.getToken().getExpiresAt()));
            String userCodeMetadata = this.writeMap(userCode.getMetadata());
//            tAuthorization.setDeviceCodeMetadata(userCodeMetadata.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setDeviceCodeMetadata(userCodeMetadata);
        }

        //deviceCodeValue
        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
        if(deviceCode != null){
            String deviceCodeValue =  deviceCode.getToken().getTokenValue();
//            tAuthorization.setDeviceCodeValue(deviceCodeValue.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setDeviceCodeValue(deviceCodeValue);
            tAuthorization.setDeviceCodeIssuedAt(Date.from(deviceCode.getToken().getIssuedAt()));
            tAuthorization.setDeviceCodeExpiresAt(Date.from(deviceCode.getToken().getExpiresAt()));
            String deviceCodeMetadata = this.writeMap(deviceCode.getMetadata());
//            tAuthorization.setDeviceCodeMetadata(deviceCodeMetadata.getBytes(StandardCharsets.UTF_8));
            tAuthorization.setDeviceCodeMetadata(deviceCodeMetadata);
        }
    }
}
