package com.sample.authentication.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.authentication.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Custom_OAuth2AuthorizationCodeAuthenticationProvider implements AuthenticationProvider {
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";
    private static final OAuth2TokenType STATE_TOKEN_TYPE = new OAuth2TokenType("state");
    private static final StringKeyGenerator DEFAULT_STATE_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder());
    private final Log logger = LogFactory.getLog(this.getClass());
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    private OAuth2TokenGenerator<OAuth2AuthorizationCode> authorizationCodeGenerator = new OAuth2AuthorizationCodeGenerator();
    private Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> authenticationValidator = new OAuth2AuthorizationCodeRequestAuthenticationValidator();
    private Predicate<OAuth2AuthorizationCodeRequestAuthenticationContext> authorizationConsentRequired = OAuth2AuthorizationCodeRequestAuthenticationProvider::isAuthorizationConsentRequired;

    public Custom_OAuth2AuthorizationCodeAuthenticationProvider(RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationService authorizationService, OAuth2AuthorizationConsentService authorizationConsentService) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(authorizationConsentService, "authorizationConsentService cannot be null");
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationService = authorizationService;
        this.authorizationConsentService = authorizationConsentService;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken)authentication;
        OAuth2Authorization pushedAuthorization = null;
        String requestUri = (String)authorizationCodeRequestAuthentication.getAdditionalParameters().get("request_uri");
        if (StringUtils.hasText(requestUri)) {
            OAuth2PushedAuthorizationRequestUri pushedAuthorizationRequestUri = null;

            try {
                pushedAuthorizationRequestUri = OAuth2PushedAuthorizationRequestUri.parse(requestUri);
            } catch (Exception var16) {
                throwError("invalid_request", "request_uri", authorizationCodeRequestAuthentication, (RegisteredClient)null);
            }

            pushedAuthorization = this.authorizationService.findByToken(pushedAuthorizationRequestUri.getState(), STATE_TOKEN_TYPE);
            if (pushedAuthorization == null) {
                throwError("invalid_request", "request_uri", authorizationCodeRequestAuthentication, (RegisteredClient)null);
            }

            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Retrieved authorization with pushed authorization request");
            }

            /**
             *
             */
            //OAuth2AuthorizationRequest authorizationRequest = (OAuth2AuthorizationRequest)pushedAuthorization.getAttribute(OAuth2AuthorizationRequest.class.getName());

            Map<String,Object> map  = pushedAuthorization.getAttribute(OAuth2AuthorizationRequest.class.getName());

            String cleanStr = map.get("scopes").toString().replace("[", "").replace("]", "").replace(" ", "");
            Set<String> scopeSet = new HashSet<>(Arrays.asList(cleanStr.split(",")));
            System.out.println(scopeSet); // [openid, profile]（重复元素已去重）

            OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode();
            OAuth2AuthorizationRequest authorizationRequest = builder.clientId(map.get("clientId").toString())
                    .authorizationUri(map.get("authorizationUri").toString())
                    .redirectUri(map.get("authorizationUri").toString())
                    .attributes((Map)map.get("attributes"))
                    .state(map.get("state").toString())
                    .scopes(scopeSet)
                    .additionalParameters((Map)map.get("additionalParameters"))
                    .build();


            if (!authorizationCodeRequestAuthentication.getClientId().equals(authorizationRequest.getClientId())) {
                throwError("invalid_request", "client_id", authorizationCodeRequestAuthentication, (RegisteredClient)null);
            }

            if (Instant.now().isAfter(pushedAuthorizationRequestUri.getExpiresAt())) {
                this.authorizationService.remove(pushedAuthorization);
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn(LogMessage.format("Removed expired pushed authorization request for client id '%s'", authorizationRequest.getClientId()));
                }

                throwError("invalid_request", "request_uri", authorizationCodeRequestAuthentication, (RegisteredClient)null);
            }

            authorizationCodeRequestAuthentication = new OAuth2AuthorizationCodeRequestAuthenticationToken(authorizationCodeRequestAuthentication.getAuthorizationUri(), authorizationRequest.getClientId(), (Authentication)authorizationCodeRequestAuthentication.getPrincipal(), authorizationRequest.getRedirectUri(), authorizationRequest.getState(), authorizationRequest.getScopes(), authorizationRequest.getAdditionalParameters());
        }

        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(authorizationCodeRequestAuthentication.getClientId());
        if (registeredClient == null) {
            throwError("invalid_request", "client_id", authorizationCodeRequestAuthentication, (RegisteredClient)null);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Retrieved registered client");
        }

        OAuth2AuthorizationCodeRequestAuthenticationContext.Builder authenticationContextBuilder = OAuth2AuthorizationCodeRequestAuthenticationContext.with(authorizationCodeRequestAuthentication).registeredClient(registeredClient);
        OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext = authenticationContextBuilder.build();
        OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_AUTHORIZATION_GRANT_TYPE_VALIDATOR.accept(authenticationContext);
        this.authenticationValidator.accept(authenticationContext);
        OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_CODE_CHALLENGE_VALIDATOR.accept(authenticationContext);
        Set<String> promptValues = Collections.emptySet();
        if (authorizationCodeRequestAuthentication.getScopes().contains("openid")) {
            String prompt = (String)authorizationCodeRequestAuthentication.getAdditionalParameters().get("prompt");
            if (StringUtils.hasText(prompt)) {
                OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_PROMPT_VALIDATOR.accept(authenticationContext);
                promptValues = new HashSet(Arrays.asList(StringUtils.delimitedListToStringArray(prompt, " ")));
            }
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Validated authorization code request parameters");
        }

        Authentication principal = (Authentication)authorizationCodeRequestAuthentication.getPrincipal();
        if (!isPrincipalAuthenticated(principal)) {
            if (((Set)promptValues).contains("none")) {
                throwError("login_required", "prompt", authorizationCodeRequestAuthentication, registeredClient);
            }

            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Did not authenticate authorization code request since principal not authenticated");
            }

            return authorizationCodeRequestAuthentication;
        } else {
            OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode().authorizationUri(authorizationCodeRequestAuthentication.getAuthorizationUri()).clientId(registeredClient.getClientId()).redirectUri(authorizationCodeRequestAuthentication.getRedirectUri()).scopes(authorizationCodeRequestAuthentication.getScopes()).state(authorizationCodeRequestAuthentication.getState()).additionalParameters(authorizationCodeRequestAuthentication.getAdditionalParameters()).build();
            authenticationContextBuilder.authorizationRequest(authorizationRequest);
            OAuth2AuthorizationConsent currentAuthorizationConsent = this.authorizationConsentService.findById(registeredClient.getId(), principal.getName());
            if (currentAuthorizationConsent != null) {
                authenticationContextBuilder.authorizationConsent(currentAuthorizationConsent);
            }

            if (this.authorizationConsentRequired.test(authenticationContextBuilder.build())) {
                if (((Set)promptValues).contains("none")) {
                    throwError("consent_required", "prompt", authorizationCodeRequestAuthentication, registeredClient);
                }

                String state = DEFAULT_STATE_GENERATOR.generateKey();
                OAuth2Authorization authorization = authorizationBuilder(registeredClient, principal, authorizationRequest).attribute("state", state).build();
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Generated authorization consent state");
                }

                this.authorizationService.save(authorization);
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Saved authorization");
                }

                if (pushedAuthorization != null) {
                    this.authorizationService.remove(pushedAuthorization);
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Removed authorization with pushed authorization request");
                    }
                }

                Set<String> currentAuthorizedScopes = currentAuthorizationConsent != null ? currentAuthorizationConsent.getScopes() : null;
                return new OAuth2AuthorizationConsentAuthenticationToken(authorizationRequest.getAuthorizationUri(), registeredClient.getClientId(), principal, state, currentAuthorizedScopes, (Map)null);
            } else {
                OAuth2TokenContext tokenContext = createAuthorizationCodeTokenContext(authorizationCodeRequestAuthentication, registeredClient, (OAuth2Authorization)null, authorizationRequest.getScopes());
                OAuth2AuthorizationCode authorizationCode = (OAuth2AuthorizationCode)this.authorizationCodeGenerator.generate(tokenContext);
                if (authorizationCode == null) {
                    OAuth2Error error = new OAuth2Error("server_error", "The token generator failed to generate the authorization code.", "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1");
                    throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, (OAuth2AuthorizationCodeRequestAuthenticationToken)null);
                } else {
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Generated authorization code");
                    }

                    OAuth2Authorization authorization = authorizationBuilder(registeredClient, principal, authorizationRequest).authorizedScopes(authorizationRequest.getScopes()).token(authorizationCode).build();
                    this.authorizationService.save(authorization);
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Saved authorization");
                    }

                    if (pushedAuthorization != null) {
                        this.authorizationService.remove(pushedAuthorization);
                        if (this.logger.isTraceEnabled()) {
                            this.logger.trace("Removed authorization with pushed authorization request");
                        }
                    }

                    String redirectUri = authorizationRequest.getRedirectUri();
                    if (!StringUtils.hasText(redirectUri)) {
                        redirectUri = (String)registeredClient.getRedirectUris().iterator().next();
                    }

                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Authenticated authorization code request");
                    }

                    return new OAuth2AuthorizationCodeRequestAuthenticationToken(authorizationRequest.getAuthorizationUri(), registeredClient.getClientId(), principal, authorizationCode, redirectUri, authorizationRequest.getState(), authorizationRequest.getScopes());
                }
            }
        }
    }

    public boolean supports(Class<?> authentication) {
        return OAuth2AuthorizationCodeRequestAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setAuthorizationCodeGenerator(OAuth2TokenGenerator<OAuth2AuthorizationCode> authorizationCodeGenerator) {
        Assert.notNull(authorizationCodeGenerator, "authorizationCodeGenerator cannot be null");
        this.authorizationCodeGenerator = authorizationCodeGenerator;
    }

    public void setAuthenticationValidator(Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> authenticationValidator) {
        Assert.notNull(authenticationValidator, "authenticationValidator cannot be null");
        this.authenticationValidator = authenticationValidator;
    }

    public void setAuthorizationConsentRequired(Predicate<OAuth2AuthorizationCodeRequestAuthenticationContext> authorizationConsentRequired) {
        Assert.notNull(authorizationConsentRequired, "authorizationConsentRequired cannot be null");
        this.authorizationConsentRequired = authorizationConsentRequired;
    }

    private static boolean isAuthorizationConsentRequired(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        if (!authenticationContext.getRegisteredClient().getClientSettings().isRequireAuthorizationConsent()) {
            return false;
        } else if (authenticationContext.getAuthorizationRequest().getScopes().contains("openid") && authenticationContext.getAuthorizationRequest().getScopes().size() == 1) {
            return false;
        } else {
            return authenticationContext.getAuthorizationConsent() == null || !authenticationContext.getAuthorizationConsent().getScopes().containsAll(authenticationContext.getAuthorizationRequest().getScopes());
        }
    }

    private static OAuth2Authorization.Builder authorizationBuilder(RegisteredClient registeredClient, Authentication principal, OAuth2AuthorizationRequest authorizationRequest) {
        return OAuth2Authorization.withRegisteredClient(registeredClient).principalName(principal.getName()).authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).attribute(Principal.class.getName(), principal).attribute(OAuth2AuthorizationRequest.class.getName(), authorizationRequest);
    }

    private static OAuth2TokenContext createAuthorizationCodeTokenContext(OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2Authorization authorization, Set<String> authorizedScopes) {
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = (DefaultOAuth2TokenContext.Builder)((DefaultOAuth2TokenContext.Builder)((DefaultOAuth2TokenContext.Builder)((DefaultOAuth2TokenContext.Builder)((DefaultOAuth2TokenContext.Builder)((DefaultOAuth2TokenContext.Builder)((DefaultOAuth2TokenContext.Builder)DefaultOAuth2TokenContext.builder().registeredClient(registeredClient)).principal((Authentication)authorizationCodeRequestAuthentication.getPrincipal())).authorizationServerContext(AuthorizationServerContextHolder.getContext())).tokenType(new OAuth2TokenType("code"))).authorizedScopes(authorizedScopes)).authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)).authorizationGrant(authorizationCodeRequestAuthentication);
        if (authorization != null) {
            tokenContextBuilder.authorization(authorization);
        }

        return tokenContextBuilder.build();
    }

    private static boolean isPrincipalAuthenticated(Authentication principal) {
        return principal != null && !AnonymousAuthenticationToken.class.isAssignableFrom(principal.getClass()) && principal.isAuthenticated();
    }

    private static void throwError(String errorCode, String parameterName, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient) {
        throwError(errorCode, parameterName, "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1", authorizationCodeRequestAuthentication, registeredClient, (OAuth2AuthorizationRequest)null);
    }

    private static void throwError(String errorCode, String parameterName, String errorUri, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throwError(error, parameterName, authorizationCodeRequestAuthentication, registeredClient, authorizationRequest);
    }

    private static void throwError(OAuth2Error error, String parameterName, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
        String redirectUri = resolveRedirectUri(authorizationCodeRequestAuthentication, authorizationRequest, registeredClient);
        if (error.getErrorCode().equals("invalid_request") && (parameterName.equals("client_id") || parameterName.equals("state"))) {
            redirectUri = null;
        }

        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthenticationResult = new OAuth2AuthorizationCodeRequestAuthenticationToken(authorizationCodeRequestAuthentication.getAuthorizationUri(), authorizationCodeRequestAuthentication.getClientId(), (Authentication)authorizationCodeRequestAuthentication.getPrincipal(), redirectUri, authorizationCodeRequestAuthentication.getState(), authorizationCodeRequestAuthentication.getScopes(), authorizationCodeRequestAuthentication.getAdditionalParameters());
        throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, authorizationCodeRequestAuthenticationResult);
    }

    private static String resolveRedirectUri(OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, OAuth2AuthorizationRequest authorizationRequest, RegisteredClient registeredClient) {
        if (authorizationCodeRequestAuthentication != null && StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())) {
            return authorizationCodeRequestAuthentication.getRedirectUri();
        } else if (authorizationRequest != null && StringUtils.hasText(authorizationRequest.getRedirectUri())) {
            return authorizationRequest.getRedirectUri();
        } else {
            return registeredClient != null ? (String)registeredClient.getRedirectUris().iterator().next() : null;
        }
    }

}
