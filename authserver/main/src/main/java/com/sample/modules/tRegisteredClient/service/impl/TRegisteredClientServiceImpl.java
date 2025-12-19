package com.sample.modules.tRegisteredClient.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sample.modules.tRegisteredClient.entity.TRegisteredClient;
import com.sample.modules.tRegisteredClient.mapper.TRegisteredClientMapper;
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

import java.util.Set;
import java.util.function.Consumer;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Li.HongKun
 * @since 2025-11-12 20:18:22
 */
@Service
public class TRegisteredClientServiceImpl extends ServiceImpl<TRegisteredClientMapper, TRegisteredClient> implements TRegisteredClientService,RegisteredClientRepository {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        // RegisteredClient existingRegisteredClient = findBy(PK_FILTER, registeredClient.getId());
        RegisteredClient existingRegisteredClient = this.findById(registeredClient.getId());

        TRegisteredClient tRegisteredClient = new TRegisteredClient();
        tRegisteredClient.setId(registeredClient.getId());
        tRegisteredClient.setClientId(registeredClient.getClientId());

//        TRegisteredClient.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt()==null?null:Date.from(registeredClient.getClientIdIssuedAt()));
        tRegisteredClient.setClientSecret(registeredClient.getClientSecret());

//        TRegisteredClient.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt()==null?null:Date.from(registeredClient.getClientSecretExpiresAt()));

        tRegisteredClient.setClientName(registeredClient.getClientName());

        String clientAuthenticationMethods = StringUtils.join(registeredClient.getClientAuthenticationMethods().toArray(),",");
        tRegisteredClient.setClientAuthenticationMethods(clientAuthenticationMethods);

        String authorizationGrantTypes = StringUtils.join(registeredClient.getAuthorizationGrantTypes().toArray(),",");
        tRegisteredClient.setAuthorizationGrantTypes(authorizationGrantTypes);

        String redirectUris = StringUtils.join(registeredClient.getRedirectUris().toArray(),",");
        tRegisteredClient.setRedirectUris(redirectUris);

        tRegisteredClient.setPostLogoutRedirectUris(StringUtils.join(registeredClient.getPostLogoutRedirectUris().toArray(),","));

//        TRegisteredClient.setClientSettings(writeMap(ClientSettings.builder().requireAuthorizationConsent(true).build()));
//        TRegisteredClient.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

        tRegisteredClient.setScopes(StringUtils.join(registeredClient.getScopes().toArray(),","));
        tRegisteredClient.setClientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build().toString());

        this.saveOrUpdate(tRegisteredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        TRegisteredClient TRegisteredClient =  this.getById(id);
        return this.builder(TRegisteredClient);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        LambdaQueryChainWrapper<TRegisteredClient> wrapper =  this.lambdaQuery();
        wrapper.eq(TRegisteredClient::getClientId,clientId);
        TRegisteredClient tRegisteredClient =  wrapper.one();
        return this.builder(tRegisteredClient);
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
}
