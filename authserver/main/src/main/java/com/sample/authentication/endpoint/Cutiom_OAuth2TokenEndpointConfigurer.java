//package com.sample.authentication.endpoint;
//
//import com.sample.authentication.configurer.Custom_OAuth2ConfigurerUtils;
//import jakarta.servlet.Filter;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.ObjectPostProcessor;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.AbstractOAuth2Configurer;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2ConfigurerUtils;
//import Cutiom_OAuth2TokenEndpointConfigurer;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.oauth2.core.OAuth2Token;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.authentication.*;
//import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
//import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
//import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
//import org.springframework.security.oauth2.server.authorization.web.authentication.*;
//import org.springframework.security.web.access.intercept.AuthorizationFilter;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
//import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.util.Assert;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
//public abstract class Cutiom_OAuth2TokenEndpointConfigurer  extends AbstractOAuth2Configurer {
//        private RequestMatcher requestMatcher;
//        private final List<AuthenticationConverter> accessTokenRequestConverters = new ArrayList();
//        private Consumer<List<AuthenticationConverter>> accessTokenRequestConvertersConsumer = (accessTokenRequestConverters) -> {
//        };
//        private final List<AuthenticationProvider> authenticationProviders = new ArrayList();
//        private Consumer<List<AuthenticationProvider>> authenticationProvidersConsumer = (authenticationProviders) -> {
//        };
//        private AuthenticationSuccessHandler accessTokenResponseHandler;
//        private AuthenticationFailureHandler errorResponseHandler;
//
//        Cutiom_OAuth2TokenEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
//            super(objectPostProcessor);
//        }
//
//        public Cutiom_OAuth2TokenEndpointConfigurer accessTokenRequestConverter(AuthenticationConverter accessTokenRequestConverter) {
//            Assert.notNull(accessTokenRequestConverter, "accessTokenRequestConverter cannot be null");
//            this.accessTokenRequestConverters.add(accessTokenRequestConverter);
//            return this;
//        }
//
//        public Cutiom_OAuth2TokenEndpointConfigurer accessTokenRequestConverters(Consumer<List<AuthenticationConverter>> accessTokenRequestConvertersConsumer) {
//            Assert.notNull(accessTokenRequestConvertersConsumer, "accessTokenRequestConvertersConsumer cannot be null");
//            this.accessTokenRequestConvertersConsumer = accessTokenRequestConvertersConsumer;
//            return this;
//        }
//
//        public Cutiom_OAuth2TokenEndpointConfigurer authenticationProvider(AuthenticationProvider authenticationProvider) {
//            Assert.notNull(authenticationProvider, "authenticationProvider cannot be null");
//            this.authenticationProviders.add(authenticationProvider);
//            return this;
//        }
//
//        public Cutiom_OAuth2TokenEndpointConfigurer authenticationProviders(Consumer<List<AuthenticationProvider>> authenticationProvidersConsumer) {
//            Assert.notNull(authenticationProvidersConsumer, "authenticationProvidersConsumer cannot be null");
//            this.authenticationProvidersConsumer = authenticationProvidersConsumer;
//            return this;
//        }
//
//        public Cutiom_OAuth2TokenEndpointConfigurer accessTokenResponseHandler(AuthenticationSuccessHandler accessTokenResponseHandler) {
//            this.accessTokenResponseHandler = accessTokenResponseHandler;
//            return this;
//        }
//
//        public Cutiom_OAuth2TokenEndpointConfigurer errorResponseHandler(AuthenticationFailureHandler errorResponseHandler) {
//            this.errorResponseHandler = errorResponseHandler;
//            return this;
//        }
//
//        void init(HttpSecurity httpSecurity) {
//            AuthorizationServerSettings authorizationServerSettings = Custom_OAuth2ConfigurerUtils.getAuthorizationServerSettings(httpSecurity);
//            String tokenEndpointUri = authorizationServerSettings.isMultipleIssuersAllowed() ? Custom_OAuth2ConfigurerUtils.withMultipleIssuersPattern(authorizationServerSettings.getTokenEndpoint()) : authorizationServerSettings.getTokenEndpoint();
//            this.requestMatcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, tokenEndpointUri);
//            List<AuthenticationProvider> authenticationProviders = createDefaultAuthenticationProviders(httpSecurity);
//            if (!this.authenticationProviders.isEmpty()) {
//                authenticationProviders.addAll(0, this.authenticationProviders);
//            }
//
//            this.authenticationProvidersConsumer.accept(authenticationProviders);
//            authenticationProviders.forEach((authenticationProvider) -> {
//                httpSecurity.authenticationProvider((AuthenticationProvider)this.postProcess(authenticationProvider));
//            });
//        }
//
//        void configure(HttpSecurity httpSecurity) {
//            AuthenticationManager authenticationManager = (AuthenticationManager)httpSecurity.getSharedObject(AuthenticationManager.class);
//            AuthorizationServerSettings authorizationServerSettings = Custom_OAuth2ConfigurerUtils.getAuthorizationServerSettings(httpSecurity);
//            String tokenEndpointUri = authorizationServerSettings.isMultipleIssuersAllowed() ? Custom_OAuth2ConfigurerUtils.withMultipleIssuersPattern(authorizationServerSettings.getTokenEndpoint()) : authorizationServerSettings.getTokenEndpoint();
//            OAuth2TokenEndpointFilter tokenEndpointFilter = new OAuth2TokenEndpointFilter(authenticationManager, tokenEndpointUri);
//            List<AuthenticationConverter> authenticationConverters = createDefaultAuthenticationConverters();
//            if (!this.accessTokenRequestConverters.isEmpty()) {
//                authenticationConverters.addAll(0, this.accessTokenRequestConverters);
//            }
//
//            this.accessTokenRequestConvertersConsumer.accept(authenticationConverters);
//            tokenEndpointFilter.setAuthenticationConverter(new DelegatingAuthenticationConverter(authenticationConverters));
//            if (this.accessTokenResponseHandler != null) {
//                tokenEndpointFilter.setAuthenticationSuccessHandler(this.accessTokenResponseHandler);
//            }
//
//            if (this.errorResponseHandler != null) {
//                tokenEndpointFilter.setAuthenticationFailureHandler(this.errorResponseHandler);
//            }
//
//            httpSecurity.addFilterAfter((Filter)this.postProcess(tokenEndpointFilter), AuthorizationFilter.class);
//        }
//
//        RequestMatcher getRequestMatcher() {
//            return this.requestMatcher;
//        }
//
//        private static List<AuthenticationConverter> createDefaultAuthenticationConverters() {
//            List<AuthenticationConverter> authenticationConverters = new ArrayList();
//            authenticationConverters.add(new OAuth2AuthorizationCodeAuthenticationConverter());
//            authenticationConverters.add(new OAuth2RefreshTokenAuthenticationConverter());
//            authenticationConverters.add(new OAuth2ClientCredentialsAuthenticationConverter());
//            authenticationConverters.add(new OAuth2DeviceCodeAuthenticationConverter());
//            authenticationConverters.add(new OAuth2TokenExchangeAuthenticationConverter());
//            return authenticationConverters;
//        }
//
//        private static List<AuthenticationProvider> createDefaultAuthenticationProviders(HttpSecurity httpSecurity) {
//            List<AuthenticationProvider> authenticationProviders = new ArrayList();
//            OAuth2AuthorizationService authorizationService = Custom_OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
//            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = Custom_OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
//            OAuth2AuthorizationCodeAuthenticationProvider authorizationCodeAuthenticationProvider = new OAuth2AuthorizationCodeAuthenticationProvider(authorizationService, tokenGenerator);
//            SessionRegistry sessionRegistry = (SessionRegistry)httpSecurity.getSharedObject(SessionRegistry.class);
//            if (sessionRegistry != null) {
//                authorizationCodeAuthenticationProvider.setSessionRegistry(sessionRegistry);
//            }
//
//            authenticationProviders.add(authorizationCodeAuthenticationProvider);
//            OAuth2RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider = new OAuth2RefreshTokenAuthenticationProvider(authorizationService, tokenGenerator);
//            authenticationProviders.add(refreshTokenAuthenticationProvider);
//            OAuth2ClientCredentialsAuthenticationProvider clientCredentialsAuthenticationProvider = new OAuth2ClientCredentialsAuthenticationProvider(authorizationService, tokenGenerator);
//            authenticationProviders.add(clientCredentialsAuthenticationProvider);
//            OAuth2DeviceCodeAuthenticationProvider deviceCodeAuthenticationProvider = new OAuth2DeviceCodeAuthenticationProvider(authorizationService, tokenGenerator);
//            authenticationProviders.add(deviceCodeAuthenticationProvider);
//            OAuth2TokenExchangeAuthenticationProvider tokenExchangeAuthenticationProvider = new OAuth2TokenExchangeAuthenticationProvider(authorizationService, tokenGenerator);
//            authenticationProviders.add(tokenExchangeAuthenticationProvider);
//            return authenticationProviders;
//        }
//}
