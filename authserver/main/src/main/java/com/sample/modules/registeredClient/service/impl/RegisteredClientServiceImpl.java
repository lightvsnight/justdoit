package com.sample.modules.registeredClient.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.modules.tRegisteredClient.entity.TRegisteredClient;
import com.sample.modules.tRegisteredClient.service.TRegisteredClientService;
import com.sample.utils.ListHandleConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Service
public class RegisteredClientServiceImpl implements RegisteredClientRepository {

    private final Log logger = LogFactory.getLog(getClass());
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TRegisteredClientService tregisteredClientService;

    @Override
    public void save(RegisteredClient registeredClient) {

        Assert.notNull(registeredClient, "registeredClient cannot be null");
//        RegisteredClient existingRegisteredClient = this.findById( registeredClient.getId());

        TRegisteredClient TRegisteredClient = new TRegisteredClient();
        TRegisteredClient.setId(registeredClient.getId());
        TRegisteredClient.setClientId(registeredClient.getClientId());
        TRegisteredClient.setClientIdIssuedAt(Date.from(registeredClient.getClientIdIssuedAt()));
        TRegisteredClient.setClientSecret(registeredClient.getClientSecret());
        TRegisteredClient.setClientSecretExpiresAt(Date.from(registeredClient.getClientSecretExpiresAt()));
        TRegisteredClient.setClientName(registeredClient.getClientName());
        String clientAuthenticationMethods = StringUtils.join(registeredClient.getClientAuthenticationMethods().toArray(),",");
        TRegisteredClient.setClientAuthenticationMethods(clientAuthenticationMethods);
        String authorizationGrantTypes = StringUtils.join(registeredClient.getAuthorizationGrantTypes().toArray(),",");
        TRegisteredClient.setAuthorizationGrantTypes(authorizationGrantTypes);
        String redirectUris = StringUtils.join(registeredClient.getRedirectUris().toArray(),",");
        TRegisteredClient.setRedirectUris(redirectUris);
        TRegisteredClient.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        TRegisteredClient.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        TRegisteredClient TRegisteredClient =  tregisteredClientService.getById(id);
        return this.builder(TRegisteredClient);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        LambdaQueryChainWrapper<TRegisteredClient> wrapper =  tregisteredClientService.lambdaQuery();
        wrapper.eq(TRegisteredClient::getClientId,clientId);
        TRegisteredClient TRegisteredClient =  wrapper.one();
        return this.builder(TRegisteredClient);
    }

    private RegisteredClient builder(TRegisteredClient tRegisteredClient){
        RegisteredClient registeredClient = null;
        if(tRegisteredClient != null){
            registeredClient = RegisteredClient.withId(tRegisteredClient.getId())
                    .clientId(tRegisteredClient.getClientId())
                    .clientSecret(tRegisteredClient.getClientSecret())
                    .clientAuthenticationMethods(new Consumer<Set<ClientAuthenticationMethod>>() {
                        @Override
                        public void accept(Set<ClientAuthenticationMethod> clientAuthenticationMethods) {
                            for(String method : tRegisteredClient.getClientAuthenticationMethods().split(",")){
                                if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(method)) {
                                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                                } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(method)) {
                                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                                } else {
                                    clientAuthenticationMethods.add(ClientAuthenticationMethod.NONE);
                                }

                            }
                        }
                    })
                    .authorizationGrantTypes(new Consumer<Set<AuthorizationGrantType>>() {
                        @Override
                        public void accept(Set<AuthorizationGrantType> authorizationGrantTypes) {
                            for(String type : tRegisteredClient.getAuthorizationGrantTypes().split(",")){
                                if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(type)) {
                                    authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                                } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(type)) {
                                    authorizationGrantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                                } else {
                                    authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                                }
                            }
                        }
                    })
                    .redirectUris(new ListHandleConsumer<Set<String>>(tRegisteredClient.getRedirectUris().split(",")))
                    //scope 可不可以标示客户端的权限
                    .scopes(new ListHandleConsumer<Set<String>>(tRegisteredClient.getScopes().split(",")))
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .build();
        }
        return registeredClient;
    }


    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception var3) {
            throw new IllegalArgumentException(var3.getMessage(), var3);
        }
    }
}
