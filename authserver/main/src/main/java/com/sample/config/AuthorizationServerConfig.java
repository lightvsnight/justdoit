/*
 * Copyright 2020-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sample.config;

import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.sample.authentication.DeviceClientAuthenticationProvider;
import com.sample.authentication.provider.Custom_ClientSecretAuthenticationProvider;
import com.sample.authentication.provider.Custom_OAuth2AuthorizationCodeAuthenticationProvider;
import com.sample.authentication.provider.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import com.sample.federation.FederatedIdentityIdTokenCustomizer;
import com.sample.jose.Jwks;

import com.sample.modules.tAuthorization.service.TAuthorizationService;
//import com.sample.modules.tRegisteredClient.service.TRegisteredClientService;
import com.sample.modules.tRegisteredClient.service.TRegisteredClientService;
import com.sample.web.authentication.DeviceClientAuthenticationConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.stereotype.Repository;


/**
 *
 * @author Li HongKun
 * @since 1.1
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

	/**
	 * 当前配置地址是，默认授权地址。
	 */
	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";


	@Autowired
	private TAuthorizationService tAuthorizationService;

	@Autowired
	private TRegisteredClientService tRegisteredClientService;


	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(
			HttpSecurity http, RegisteredClientRepository registeredClientRepository,JdbcOAuth2AuthorizationConsentService consentService,
			AuthorizationServerSettings authorizationServerSettings) throws Exception {

		/*
		 * This sample demonstrates the use of a public client that does not
		 * store credentials or authenticate with the authorization server.
		 *
		 * The following components show how to customize the authorization
		 * server to allow for device clients to perform requests to the
		 * OAuth 2.0 Device Authorization Endpoint and Token Endpoint without
		 * a clientId/clientSecret.
		 *
		 * CAUTION: These endpoints will not require any authentication, and can
		 * be accessed by any client that has a valid clientId.
		 *
		 * It is therefore RECOMMENDED to carefully monitor the use of these
		 * endpoints and employ any additional protections as needed, which is
		 * outside the scope of this sample.
		 */
		DeviceClientAuthenticationConverter deviceClientAuthenticationConverter =
				new DeviceClientAuthenticationConverter(
						authorizationServerSettings.getDeviceAuthorizationEndpoint());

		DeviceClientAuthenticationProvider deviceClientAuthenticationProvider =
				new DeviceClientAuthenticationProvider(registeredClientRepository);

		OAuth2AuthorizationCodeRequestAuthenticationProvider oAuth2AuthorizationCodeRequestAuthenticationProvider =
				new OAuth2AuthorizationCodeRequestAuthenticationProvider(registeredClientRepository, tAuthorizationService, consentService);

//		ClientSecretAuthenticationProvider clientSecretAuthenticationProvider = new ClientSecretAuthenticationProvider(registeredClientRepository,tAuthorizationService);
		Custom_OAuth2AuthorizationCodeAuthenticationProvider auth2AuthorizationCodeAuthenticationProvider =
				new Custom_OAuth2AuthorizationCodeAuthenticationProvider(registeredClientRepository,tAuthorizationService,consentService);

		Custom_ClientSecretAuthenticationProvider clientSecretAuthenticationProvider = new Custom_ClientSecretAuthenticationProvider(registeredClientRepository,tAuthorizationService);


		/**
		 * 依赖 spring-security 7.0.2时，可选择这样的写法。
		 */
//		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
		/**
		 * 依赖 spring-authorization-server 时，是这样的写法。
		 */
//		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = authorizationServer();
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();


		// @formatter:off
		http
				.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
				.with(authorizationServerConfigurer, (authorizationServer) ->
						authorizationServer
								.deviceAuthorizationEndpoint(deviceAuthorizationEndpoint ->
										deviceAuthorizationEndpoint.verificationUri("/activate")
								)
								.deviceVerificationEndpoint(deviceVerificationEndpoint ->
										deviceVerificationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI)
								)
								.clientAuthentication(clientAuthentication ->
										clientAuthentication
												.authenticationConverter(deviceClientAuthenticationConverter)
												.authenticationProvider(deviceClientAuthenticationProvider)
												.authenticationProvider(clientSecretAuthenticationProvider)
//												.authenticationProvider(auth2AuthorizationCodeAuthenticationProvider)
								)
								.authorizationEndpoint(authorizationEndpoint ->
										authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI)
												.authenticationProvider(auth2AuthorizationCodeAuthenticationProvider)
												.authenticationProvider(oAuth2AuthorizationCodeRequestAuthenticationProvider)
								)
								.authorizationService(tAuthorizationService)
//								.registeredClientRepository()
								.oidc(Customizer.withDefaults())	// Enable OpenID Connect 1.0
								.tokenEndpoint(oAuth2TokenEndpointConfigurer ->
										oAuth2TokenEndpointConfigurer.authenticationProvider(auth2AuthorizationCodeAuthenticationProvider))
				)
				.authorizeHttpRequests((authorize) ->
						authorize.anyRequest().authenticated()
				)
				// Redirect to the /login page when not authenticated from the authorization endpoint
				// NOTE: DefaultSecurityConfig is configured with formLogin.loginPage("/login")
				.exceptionHandling((exceptions) -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
						)
				);
		// @formatter:on
		return http.build();
	}


//	/**
//	 * 以下内容，通过jdbc，初始化客户端信息。
//	 *  需要重写一下存储逻辑，在以下存储逻辑中，我们只能使用，springoauth2给出的固定表
//	 *  registeredClientRepository(): TheRegisteredClientRepository(必填项) 用于管理新客户和现有客户。
//	 * @param jdbcTemplate
//	 * @return
//	 */
//	// @formatter:off
//	@Bean
//	public JdbcRegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {// Save registered client's in db as if in-memory
//
//		// Save registered client's in db as if in-memory
//		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
//		/**
//		 * 在保存前，我们应验证即将存入的数据是否唯一，如果库中已存在，则无法完成当前操作
//		 */
//		RegisteredClient registeredClient = registeredClientRepository.findByClientId("messaging-client");
//		if(registeredClient == null){
//
//			RegisteredClient messagingClient = RegisteredClient.withId(UUID.randomUUID().toString())
//					.clientId("messaging-client")
//					.clientSecret("{noop}secret")
//					.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//					.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//					.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//					.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//					.redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
//					.redirectUri("http://127.0.0.1:8080/authorized")
//					.postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
//					.scope(OidcScopes.OPENID)
//					.scope(OidcScopes.PROFILE)
//					.scope("message.read")
//					.scope("message.write")
//					.scope("user.read")
//					.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//					.build();
//			registeredClientRepository.save(messagingClient);
//		}
//
//		registeredClient = registeredClientRepository.findByClientId("device-messaging-client");
//		if(registeredClient == null){
//			RegisteredClient deviceClient = RegisteredClient.withId(UUID.randomUUID().toString())
//					.clientId("device-messaging-client")
//					.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//					.authorizationGrantType(AuthorizationGrantType.DEVICE_CODE)
//					.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//					.scope("message.read")
//					.scope("message.write")
//					.build();
//			registeredClientRepository.save(deviceClient);
//
//		}
//
//		registeredClient = registeredClientRepository.findByClientId("token-client");
//		if(registeredClient == null){
//		RegisteredClient tokenExchangeClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientId("token-client")
//				.clientSecret("{noop}token")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:token-exchange"))
//				.scope("message.read")
//				.scope("message.write")
//				.build();
//			registeredClientRepository.save(tokenExchangeClient);
//
//		}
//
//		registeredClient = registeredClientRepository.findByClientId("mtls-demo-client");
//		if(registeredClient == null){
//			RegisteredClient mtlsDemoClient = RegisteredClient.withId(UUID.randomUUID().toString())
//					.clientId("mtls-demo-client")
//					.clientAuthenticationMethod(ClientAuthenticationMethod.TLS_CLIENT_AUTH)
//					.clientAuthenticationMethod(ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH)
//					.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//					.scope("message.read")
//					.scope("message.write")
//					.clientSettings(
//							ClientSettings.builder()
//									.x509CertificateSubjectDN("CN=demo-client-sample,OU=Spring Samples,O=Spring,C=US")
//									.jwkSetUrl("http://127.0.0.1:8080/jwks")
//									.build()
//					)
//					.tokenSettings(
//							TokenSettings.builder()
//									.x509CertificateBoundAccessTokens(true)
//									.build()
//					)
//					.build();
//			registeredClientRepository.save(mtlsDemoClient);
//		}
//
//
//		return registeredClientRepository;
//	}
	// @formatter:on

//	@Bean
//	public JdbcOAuth2AuthorizationService jdbcAuthorizationService(JdbcTemplate jdbcTemplate,
//															   RegisteredClientRepository registeredClientRepository) {
//		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
//	}

	@Bean
	public JdbcOAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
																			 RegisteredClientRepository registeredClientRepository) {
		// Will be used by the ConsentController
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer() {
		return new FederatedIdentityIdTokenCustomizer();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/**
	 * 授权服务配置
	 * @return
	 */
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	/*@Bean
	public EmbeddedDatabase embeddedDatabase() {
		// @formatter:off
		return new EmbeddedDatabaseBuilder()
				.generateUniqueName(true)
				.setType(EmbeddedDatabaseType.HSQL)
				.setScriptEncoding("UTF-8")
				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
				.addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
				.build();
		// @formatter:on
	}*/

}
